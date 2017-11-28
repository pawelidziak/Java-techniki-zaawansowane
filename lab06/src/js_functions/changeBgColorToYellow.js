var changeBgColorToYellow = function (object) {
    //  Creates an sRGB color with the specified red, green, blue, and alpha values in the range (0.0 - 1.0).
    var yellowColor = new java.awt.Color(1.0, 1.0, 0.0, 1.0);
    object.setBackground(yellowColor);
    return object;
};