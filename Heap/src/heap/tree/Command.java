package heap.tree;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by david on 2/18/16.
 */
class Command {
    int xvalue;
    char command;

    Command() {
        command = 0;
        xvalue = 0;
    }

    public String toString() {
        return "command = " + command + " x = " + xvalue;
    }

    public void readCommand(BufferedReader in) throws InvalidCommandException, IOException, NumberFormatException {

        boolean readcommand = false;

        // until a command has been read (valid or invalid) get something from input
        while(!readcommand && in.ready()) {
            command = (char)in.read();

            // if the line is a comment output the rest of the line
            if(command == '#') {
                // print the comment to the screen
                System.out.println( in.readLine() );
            }
            // if a valid command was read get any necessary arguments
            else if(this.validCommand()) {
                if(this.commandWithArgument()) {
                    xvalue = Integer.parseInt(in.readLine().trim());
                }
                else {
                    xvalue = 0;
                    in.readLine();
                }
                readcommand = true;
            }
            else {
                // clean up the rest of the garbage on the input line
                in.readLine();
                throw new InvalidCommandException(command);
            }
        }
    }

    public char getCommand() {
        return command;
    }

    public int getXValue() {
        return xvalue;
    }

    private boolean validCommand() {
        return commandWithArgument() || commandWithoutArgument();
    }
    // list of commands that have an argument
    private boolean commandWithArgument() {
        return this.command == 'i' || this.command == 's' || this.command == 'd';
    }
    // list of commands that don't have arguments
    private boolean commandWithoutArgument() {
        return this.command == 'p' || this.command == 'q';
    }
}
