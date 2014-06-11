package net.peelo.kahvi.compiler;

import net.peelo.kahvi.compiler.ast.CompilationUnit;
import net.peelo.kahvi.compiler.parser.Parser;
import net.peelo.kahvi.compiler.parser.ParserException;
import net.peelo.kahvi.compiler.parser.Scanner;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public final class Main
{
    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; ++i)
        {
            Reader input = null;

            try
            {
                Scanner scanner;
                Parser parser;
                CompilationUnit compilationUnit;

                input = new FileReader(args[i]);
                scanner = new Scanner(input, 1, 0);
                parser = new Parser(scanner);
                compilationUnit = parser.parseCompilationUnit();
            }
            catch (IOException ex)
            {
                ex.printStackTrace(System.err);
            }
            catch (ParserException ex)
            {
                ex.printStackTrace(System.err);
            } finally {
                if (input != null)
                {
                    try
                    {
                        input.close();
                    }
                    catch (IOException ex) {}
                }
            }
        }
    }
}
