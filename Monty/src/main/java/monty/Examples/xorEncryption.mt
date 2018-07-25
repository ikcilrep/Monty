import sml.casts.ord;
import sml.casts.toChar;
import sml.data.string.lengthOfString;
import sml.data.string.charAt;
import sml.io.input;

func string encryptDecrypt string plain, string key;
	var static string encrypted "" =;
	var static int i 0 =;
	while i lengthOfString(plain) <;
		var static int ec ord(charAt(plain, i)) ord(charAt(key,i lengthOfString(key) %)) ^ =;
		encrypted toChar(ec) +=;
		i 1 +=;
	end;
	return encrypted;
end;

print "Podaj tekst do zaszyfrowania: ";
var static string plain input() =; 
println "";
print "Podaj has³o: ";
var static string key input() =;
println encryptDecrypt(plain,key);