load('E:\\STUDIA\\SEMESTR_7\\Java\\Lab\\lab06\\src\\helpers\\separateFromSelector.js');

var String = java.lang.String;
var ArrayList = Java.type('java.util.ArrayList');
var StringBuilder = java.lang.StringBuilder;
var HashMap = Java.type('java.util.HashMap');

var changeRandomLettersToX = function (object) {

    var stringText = new String(object.getText());

    // przechowuje indeksy liter nie znjadujacych sie w selektorach
    var indexArray = new ArrayList(separateFromSelector(stringText));

    // StringBuilder potrzebny by łatwo wstawiac nowe znaki
    var result = new StringBuilder(stringText);

    var myMap = new HashMap();

    // 25% of text will be changed to X
    for (var j = 0; j < indexArray.size() * 0.25; j++) {
        var randomInt = Math.floor((Math.random() * indexArray.size()));
        var index = indexArray[randomInt];

        if (myMap.get(index) === null) {
            myMap.put(index, 'X');
            result.setCharAt(index, 'X');
        } else {
            j--;
        }
    }

    object.setText(result);
    return object;
};

