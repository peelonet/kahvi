package net.peelo.kahvi.compiler.event;

import net.peelo.kahvi.compiler.ast.Node;
import net.peelo.kahvi.compiler.util.SourcePosition;

import java.util.EnumSet;
import java.util.Set;

/**
 * Implementation of compiler warning message.
 */
public final class WarningMessage extends AbstractMessage
{
    /** Type of the warning. */
    private final Kind kind;

    public WarningMessage(SourcePosition position,
                          Node source,
                          Kind kind,
                          String message)
    {
        super(position, source, message);
        this.kind = kind;
    }

    public Kind getKind()
    {
        return this.kind;
    }

    @Override
    public String toString()
    {
        return "warning: [" + this.kind + "]" + this.getMessage();
    }

    public enum Kind
    {
        /**
         * Warn about unnecessary casts.
         */
        CAST,

        /**
         * Warn about deprecated items.
         */
        DEPRECATION,

        /**
         * Warn about division by constant integer {@code 0}.
         */
        DIVZERO,

        /**
         * Warn about empty statement after {@code if}.
         */
        EMPTY,

        /**
         * Warn about falling through from one case of switch statement to the
         * next.
         */
        FALLTHROUGH,

        /**
         * Warn about finally clauses which do not terminate normally.
         */
        FINALLY,

        /**
         * Warn about issues regarding method overrides.
         */
        OVERRIDES,

        /**
         * Warn about serializable classes that do not provide a serial version ID.
         */
        SERIAL,

        /**
         * Warn about unchecked operations on raw types.
         */
        UNCHECKED,

        /**
         * Warn about loops which execute wrong.
         */
        LOOPS;

        public static Set<Kind> parse(String pattern)
        {
            Set<Kind> warnings;

            if (pattern.indexOf("*") >= 0)
            {
                return EnumSet.allOf(Kind.class);
            }
            warnings = EnumSet.noneOf(Kind.class);
            for (String part : pattern.split(","))
            {
                part = part.trim().toUpperCase();
                if (part.length() == 0)
                {
                    continue;
                }
                try
                {
                    warnings.add(Kind.valueOf(part));
                }
                catch (IllegalArgumentException ex)
                {
                    // Invalid warnings types are just ignored
                }
            }

            return warnings;
        }
    }
}
