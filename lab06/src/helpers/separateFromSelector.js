var separateFromSelector = function (text) {
    var tmpArray = new ArrayList();
    for (var j = 0; j < text.length(); j++) {
        if (text.charAt(j) === '<') {
            while (text.charAt(j) !== '>') j++;
        }
        if (text.charAt(j) !== '>' &&
            text.charAt(j) !== '\n' &&
            text.charAt(j) !== '\t' &&
            text.charAt(j) !== ' ') tmpArray.add(j)
    }
    return tmpArray;
};
