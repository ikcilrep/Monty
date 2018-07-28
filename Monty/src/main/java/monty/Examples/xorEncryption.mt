import sml.casts.ord;
import sml.casts.toChar;
import sml.data.string.lengthOfString;
import sml.data.string.charAt;
import sml.io.input;
import sml.io.print;
import sml.io.println;



func string encryptDecrypt string plain, string key;
	var static string encrypted "" =;
	var static int i 0 =;
	while i lengthOfString(plain) <;
		encrypted toChar(ord(charAt(plain, i)) ord(charAt(key,i lengthOfString(key) %)) ^) +=;
		i 1 +=;
	end;
	return encrypted;
end;

print("Podaj tekst do zaszyfrowania:\t");
var static string plain input() =; 
print("Podaj hasło: ");
var static string key input() =;
println(encryptDecrypt(plain,key));
