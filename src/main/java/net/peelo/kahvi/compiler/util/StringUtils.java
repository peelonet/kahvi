package net.peelo.kahvi.compiler.util;

/**
 * Contains various string manipulation and testing utility methods.
 */
public final class StringUtils
{
    /**
     * Tests whether character sequence contains valid Java identifier.
     */
    public static boolean isJavaIdentifier(CharSequence s)
    {
        int length;

        if (s == null
            || (length = s.length()) == 0
            || !Character.isJavaIdentifierStart(s.charAt(0)))
        {
            return false;
        }
        for (int i = 1; i < length; ++i)
        {
            if (!Character.isJavaIdentifierPart(s.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    public static String capitalize(String input)
    {
        int length;

        if (input == null || (length = input.length()) == 0)
        {
            return "";
        }
        else if (length > 1)
        {
            return input.substring(0, 1).toUpperCase().concat(input.substring(1));
        } else {
            return input.toUpperCase();
        }
    }

    public static String toGetterName(String input)
    {
        return "get".concat(StringUtils.capitalize(input));
    }

    public static String toIsGetterName(String input)
    {
        return "is".concat(StringUtils.capitalize(input));
    }

    public static String toSetterName(String input)
    {
        return "set".concat(StringUtils.capitalize(input));
    }

    /**
     * This class should not be instanced.
     */
    private StringUtils() {}
}
