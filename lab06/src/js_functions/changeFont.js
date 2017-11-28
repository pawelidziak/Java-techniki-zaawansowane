var changeFont = function (object) {
    //  Creates an sRGB color with the specified red, green, blue, and alpha values in the range (0.0 - 1.0).
    var font = new java.awt.Font('Segoe Script', 0, 15);
    object.setFont(font);
    return object;
};