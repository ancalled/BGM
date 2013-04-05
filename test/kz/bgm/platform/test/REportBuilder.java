//package kz.bgm.platform.test;
//
//import kz.bgm.platform.items.ReportItem;
//import kz.bgm.platform.items.Track;
//import kz.bgm.platform.service.CatalogStorage;
//
//import java.util.List;
//
//public class ReportBuilder {
//
//    public static final boolean DEBUG = false;
//
//
//    public static void buildMobileReport(CatalogStorage catalog, List<ReportItem> reportItems) {
//
//        System.out.println("Building mobile report...");
//
//        int idx = 1;
////        String sep = ";";
//        String sep = "^";
//
//        for (ReportItem ri : reportItems) {
//
//            Track authTrack = catalog.search(ri.getAuthor(), ri.getCompisition(), false);
//            Track comTrack = catalog.search(ri.getAuthor(), ri.getCompisition(), true);
//
//            if (DEBUG) {
//                System.out.println(authTrack != null ? authTrack : "Not found :(");
//                System.out.println();
//            } else {
//                if (authTrack != null || comTrack != null) {
//
//                    Track comp = authTrack != null ? authTrack : comTrack;
//
//                    float authRate = 0;
//                    int authorRevenue = 0;
//                    int publisherAuthRevenue = 0;
//                    String authCatalog = "";
//                    if (authTrack != null) {
//                        authRate = authTrack.getMobileRate();
//                        authorRevenue = Math.round(ri.getQty() * ri.getPrice() * ri.getRate() * authRate / 100);
//                        publisherAuthRevenue = Math.round(authorRevenue * authTrack.getRoyalty() / 100);
//                        authCatalog = authTrack.getCatalog();
//                    }
//
//                    float commonRate = 0;
//                    int commonRevenue = 0;
//                    int publisherCommonRevenue = 0;
//                    String comCatalog = "";
//                    if (comTrack != null) {
//                        commonRate = comTrack.getMobileRate();
//                        commonRevenue = Math.round(ri.getQty() * ri.getPrice() * ri.getRate() * commonRate / 100);
//                        publisherCommonRevenue = Math.round(commonRevenue * comTrack.getRoyalty() / 100);
//                        comCatalog = comTrack.getCatalog();
//                    }
//
//                    String musicAuthors = comp.getMusicAuthors() != null ? comp.getMusicAuthors() : "";
//                    String lyricsAuthors = comp.getLyricsAuthors() != null ? comp.getLyricsAuthors() : "";
//
//                    System.out.println(
//                            idx++ + sep +
//                                    comp.getCode() + sep +
//                                    comp.getComposition() + sep +
//                                    musicAuthors + sep +
//                                    lyricsAuthors + sep +
//                                    comp.getArtist() + sep +
//                                    ri.getContentType() + sep +
//                                    authRate + sep +
//                                    ri.getQty() + sep +
//                                    ri.getPrice() + sep +
//                                    comp.getRoyalty() + sep +
//                                    authorRevenue + sep +
//                                    publisherAuthRevenue + sep +
//                                    commonRate + sep +
//                                    commonRevenue + sep +
//                                    publisherCommonRevenue + sep +
//                                    authCatalog + sep +
//                                    comCatalog + sep
//                    );
//
//                }
//            }
//        }
//    }
//
//
//    private static void buildRadioReport(CatalogStorage catalog, List<ReportItem> items) {
//        int idx = 1;
////        String sep = ";";
//        String sep = "^";
//
//        for (ReportItem item : items) {
//
//            Track track = catalog.search(item.getAuthor(), item.getCompisition(), false);
//            if (track != null) {
//                System.out.println(idx++ + sep +
//                        track.getCode() + sep +
//                        track.getComposition() + sep +
//                        track.getMusicAuthors() + sep +
//                        track.getLyricsAuthors() + sep +
//                        track.getPublicRate() + sep +
//                        track.getCatalog() + sep
//                );
//            }
//        }
//    }
//
//    public static void mergeReports(List<ReportItem> items, List<ReportItem> nextItems) {
//
//        if (items.isEmpty()) {
//            items.addAll(nextItems);
//        } else {
//            for (ReportItem ni : nextItems) {
//                ReportItem found = null;
//                for (ReportItem i : items) {
//                    if (ni.getAuthor().equalsIgnoreCase(i.getAuthor()) &&
//                            ni.getCompisition().equalsIgnoreCase(i.getCompisition()) &&
//                            ni.getContentType().equalsIgnoreCase(i.getContentType()) &&
//                            ni.getPrice() == i.getPrice()
//                            ) {
//                        found = i;
//                        break;
//                    }
//                }
//                if (found == null) {
//                    items.add(ni);
//                } else {
//                    found.setQty(found.getQty() + ni.getQty());
//                }
//            }
//        }
//    }
//
//
//}