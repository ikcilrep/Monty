import sml.io.readFile;
import sml.io.writeFile;
import sml.io.println;
import sml.io.print;
import sml.io.input;
print("Wpisz nazwę pliku:\t");
static string name input() =;
print("Wpisz tekst do zapisania w pliku:\t");
writeFile(name, input(),false); 'Write file`
print("Wpisz tekst do dodania do pliku:\t");
writeFile(name, input(),true); 'Append to file`
println(readFile(name)); 'Read file`
