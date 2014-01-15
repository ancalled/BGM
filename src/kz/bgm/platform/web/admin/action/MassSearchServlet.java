package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.SearchResult;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MassSearchServlet extends HttpServlet {

    public static final String APP_HOME = System.getProperty("user.dir");
    public static final String FILE_SEARCHES_HOME = APP_HOME + "/file-searches";

    public static final int RESULT_LIMIT = 3;
    public static final float RESULT_MIN_SCORE = 1.0f;


    private static final Logger log = Logger.getLogger(MassSearchServlet.class);

    private CatalogStorage catalogService;
    private LuceneSearch luceneSearch;
    private ServletFileUpload fileUploader;


    @Override
    public void init() throws ServletException {
        catalogService = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
        fileUploader = new ServletFileUpload(new DiskFileItemFactory());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();


        List<FileItem> fields;
        try {
            fields = fileUploader.parseRequest(req);
        } catch (FileUploadException e) {
            e.printStackTrace();
            return;
        }

        if (fields == null) {
            log.warn("No multipart fields found");
            errorNoFileReport(out, jsonObj);
            return;
        }

        FileItem item = getFileItem(fields);

        String searchFileName = FILE_SEARCHES_HOME + "/" + item.getName();
        File searchFile = new File(searchFileName);
        log.info("Saving upload to " + searchFile.getAbsolutePath());

        try {
            saveToFile(item, searchFile);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String encoding = "utf-8";
        String fieldSeparator = ",";
        boolean withHeader = true;
        int artistRow = 2;
        int trackRow = 1;

        JSONArray res =  bulkSearch(searchFile.getAbsolutePath(),
                encoding, withHeader, artistRow, trackRow,
                fieldSeparator);

        jsonObj.put("status", "ok");
        jsonObj.put("sheet", res);
        jsonObj.writeJSONString(out);

    }


    private void errorNoFileReport(PrintWriter out, JSONObject jsonObj) throws IOException {
        jsonObj.put("status", "error");
        jsonObj.put("er", "no-file-reports-uploaded");
        jsonObj.writeJSONString(out);
    }


    private FileItem getFileItem(List<FileItem> fields) {
        for (FileItem item : fields) {
            if (!item.isFormField()) {
                return item;
            }
        }

        return null;
    }


    private void saveToFile(FileItem item, File reportFile) throws Exception {

        if (!reportFile.getParentFile().exists()) {
            reportFile.getParentFile().mkdirs();
        }

        item.write(reportFile);
    }


    public JSONArray bulkSearch(String infile,
                           String encoding,
                           boolean withHeader,
                           int artistRow,
                           int trackRow,
                           String fieldSeparator)
            throws IOException {

        if (infile == null || artistRow < 0 || trackRow < 0) return null;


        List<String> lines = Files.readAllLines(Paths.get(infile), Charset.forName(encoding));


        JSONArray resArray = new JSONArray();

        int idx = 0;
        for (String line : lines) {

            String[] sourceFields = line.split(fieldSeparator);
            if (sourceFields.length <= trackRow || sourceFields.length <= artistRow) {
                continue;
            }

            String track = sourceFields[trackRow];
            String artist = sourceFields[artistRow];

            List<SearchResult> res = null;
            if (idx > 0 || !withHeader) {
                try {
                    res = luceneSearch.search(artist, artist, track, RESULT_LIMIT * 3);
                } catch (ParseException e) {
                    log.warn("Parse error", e);
                }
            }

            JSONArray row = new JSONArray();
            //noinspection unchecked
            resArray.add(row);

            Collections.addAll(row, sourceFields);
            if (withHeader && idx == 0) {
                Collections.addAll(row,
                        "Detected Artist: Track",
                        "Code",
                        "Mobile Share",
                        "Public Share",
                        "Catalog"
                );
            }

            if (res != null) {
                for (SearchResult r : filterByHighScore(res, RESULT_LIMIT)) {
                    if (r.getScore() < RESULT_MIN_SCORE) continue;

                    Track t = catalogService.getTrack(r.getTrackId());
                    if (t != null) {
                        Collections.addAll(row,
                                t.getCode(),
                                t.getCatalog(),
                                t.getMobileShare()
                        );
                    }
                }
            }
            idx++;
        }

        return resArray;
    }


    public static List<SearchResult> filterByHighScore(List<SearchResult> results, int limit) {
        if (results == null) return null;
        if (results.isEmpty()) return Collections.emptyList();

        List<SearchResult> filtered = new ArrayList<>();

        SearchResult first = results.get(0);

        filtered.add(first);

        for (int i = 1; i < Math.min(results.size(), limit); i++) {
            SearchResult sr = results.get(i);
            if (sr.getScore() < first.getScore()) {
                break;
            }
            filtered.add(sr);
        }

        return filtered;
    }



}
