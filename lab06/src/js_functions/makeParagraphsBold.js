var String = java.lang.String;
var ArrayList = Java.type('java.util.ArrayList');
var StringBuilder = java.lang.StringBuilder;
var Integer = java.lang.Integer;

var makeParagraphsBold = function (object) {

    var stringText = new String(object.getText());
    var result = new StringBuilder(stringText);

    var stringToFind1 = new String('<p>');
    var stringToFind2 = new String('</p>');
    var tagStart = new String('<b>');
    var tagEnd = new String('</b>');

    var lastIndex = stringText.indexOf(stringToFind1);
    // czary mary
    while (lastIndex >= 0) {
        if(lastIndex >= 0){
            var indexStart = new Integer(stringText.indexOf(stringToFind1, lastIndex));
            var indexStop = new Integer(stringText.indexOf(stringToFind2, lastIndex));

            var bolded = new StringBuilder();
            bolded.append(tagStart).append(result.substring(indexStart, indexStop)).append(tagEnd);
            result.replace(indexStart, indexStop, bolded);

        }
        lastIndex = stringText.indexOf(stringToFind1, lastIndex + 1);
    }

    object.setText(result.toString());
    return object;
};
