load('E:\\STUDIA\\SEMESTR_7\\Java\\Lab\\lab06\\src\\helpers\\separateFromSelector.js');

var String = java.lang.String;
var ArrayList = Java.type('java.util.ArrayList');
var StringBuilder = java.lang.StringBuilder;
var HashMap = Java.type('java.util.HashMap');

var changeRandomLettersColor = function (object) {

    var stringText = new String(object.getText());

    // przechowuje indeksy liter nie znjadujacych sie w selektorach
    var indexArray = new ArrayList(separateFromSelector(stringText));
    var size = indexArray.size();

    // StringBuilder potrzebny by łatwo wstawiac nowe znaki
    var result = new StringBuilder(stringText);

    var tagStart = '<font color="red">';
    var tagEnd = '</font>';
    var myMap = new HashMap();

    // 50% of text will be colored
    for (var j = 0; j < size * 0.5; j++) {
        var randomInt = Math.floor((Math.random() * indexArray.size()));
        var index = indexArray[randomInt];

        if (myMap.get(index) === null) {
            myMap.put(index, result.toString().charAt(index));
            var coloredLetter = new StringBuilder();
            coloredLetter.append(tagStart).append(result.toString().charAt(index)).append(tagEnd);
            result.replace(index, index + 1, coloredLetter);
            indexArray = separateFromSelector(result.toString());
        } else {
            j--;
        }
    }

    object.setText(result.toString());
    return object;
};
