
import stdlib.data.array.arrayOf;
import stdlib.data.array.extendArray;
import stdlib.data.array.getFromArray;
import stdlib.data.array.lengthOfArray;
import stdlib.data.array.subArray;
import stdlib.data.array.doesArrayContain;
import stdlib.data.array.setInArray;
import stdlib.data.array.replaceAllInArray;
import stdlib.casts.toInt;

var static array a arrayOf(1,2,3) =;
println a arrayOf(1,2,3) +;
println extendArray(a,arrayOf(1,2,3));
println lengthOfArray(a);
println toInt(getFromArray(a,2)) 1 +;
println subArray(a,1,2);
println setInArray(a,1,arrayOf(1,2,3,4));
println doesArrayContain(a,2);
println replaceAllInArray(a,3,4);