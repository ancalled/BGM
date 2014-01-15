
function parseCSV(csv, delim) {
    delim = (delim || ",");

    var pat = new RegExp((
        "(\\" + delim + "|\\r?\\n|\\r|^)" +
        "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
        "([^\"\\" + delim + "\\r\\n]*))"),
        "gi");

    var res = [[]];
    var matches = null;

    while (matches = pat.exec(csv)) {
        var d = matches[1];
        if (d.length && (d != delim)) {
            res.push([]);
        }

        var value;
        if (matches[2]) {
            value = matches[2].replace(new RegExp("\"\"", "g"), "\"");
        } else {
            value = matches[3];
        }

        res[res.length - 1].push(value);
    }

    return res;
}


String.prototype.format = function () {
    var args = arguments;

    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined' ? args[number] :
            '{' + number + '}';
    });
};


function buildTable(data, headers, from, size, tableId, tableClassName) {

    var idMarkup = tableId ? ' id="' + tableId + '"' : '';
    var classMarkup = tableClassName ? ' class="' + tableClassName + '"' : '';
    var tbl = '<table ' + idMarkup + classMarkup + '>{0}{1}</table>';

    var th = '<thead>{0}</thead>';
    var tb = '<tbody>{0}</tbody>';
    var tr = '<tr>{0}</tr>';
    var thRow = '<th>{0}</th>';
    var tdRow = '<td>{0}</td>';
    var thCon = '';
    var tbCon = '';
    var trCon = '';

    if (data[0] && headers.length < data[0].length) {
        for (i = headers.length; i < data[0].length; i++) {
            headers[i] = '';
        }
    }

    for (i = 0; i < headers.length; i++) {
        thCon += thRow.format(headers[i]);
    }

    th = th.format(tr.format(thCon));

    for (i = from; i < Math.min(size, data.length); i++) {
        var col = data[i];
        for (j = 0; j < Math.max(col.length, headers.length); j++) {
            var value = col[j];
            if (!value) {
                value = '';
            }
            tbCon += tdRow.format(value);
        }
        trCon += tr.format(tbCon);
        tbCon = '';
    }
    tb = tb.format(trCon);
    tbl = tbl.format(th, tb);

    return tbl;
}


function validateDate(data, expectedCols) {
    return data && data[0] && data[0].length >= expectedCols.length
}


function addCommas(nStr) {
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}


