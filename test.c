#include <stdio>

main() {
    @fhgvhhh
   float x,y = 5.5;
   int hex = $12AA;
   char sign='+';
   while (sign == '+') {
      printf("Знак: ");
      scanf("%c%*c", &sign);
      if (sign == '0') { break; }
      if (sign == '+' ) {
         printf("x=");
         scanf("%f%*c", &x);
         printf("y=");
         scanf("%f%*c", &y);
         switch (sign) {
            case '*':
               printf("%.2f\n", x*y);
               break;
            case '/':
               if (y ^ 0) printf("%.2f\n", x/y);
               else printf("Zero division!\n");
         }
      }
      else printf("Something went wrong with operation sign\n");
   }
}.