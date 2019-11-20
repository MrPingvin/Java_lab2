package com.company;


import java.util.*;


public class Main {


    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);                                //сканер консоли, ввод
        String input;                                                       //строка ввода


        while (true) {                                                      //бесконечный цикл работы программы
            do {                                                            //ввод суммы которую надо разменять
                System.out.print("Enter the total amount : ");
                input = in.next();                                          //считывает ввод из консоли
            } while (!isNumeric(input));                                    //самописная функция проверки на число
                                                                            //если предыдущий цикл прекратился, значит был ввод числа и можно продолжить
            int amount = Integer.parseInt(input);                           //строку в число


            List<Integer> nominalArray = new ArrayList<Integer>();         //инициализация массива для разменных монет или не монет


            do {


                System.out.print("Enter coin denominations(enter 0 to stop entering) : ");
                input = in.next();
                                                                            //снова ввод и проверки на число
                if (isNumeric(input)) {
                    nominalArray.add(Integer.parseInt(input));             //если число то добавляем в массив
                }
            } while (input.equals("0"));                   //выходим из цикла если введён 0


            nominalArray = new ArrayList<Integer>(Sort(nominalArray));
                                                                            //сортируем массив, функция sort определена ниже
            System.out.println(amount);
            System.out.println(nominalArray);


            List<Coin_quantity> nominal = new ArrayList<Coin_quantity>();   //это массив для итогового ответа, хранится класс в котором два элемента : тип монеты и сколько такой монеты нужно для ращмена
            int sum = 0;
            System.out.print(amount + " -> ");


            Exchanges(amount, sum, nominalArray, nominal, nominalArray.size()-1);                 //функция с рекурсией


            if (crutch(nominal, amount)) {

                for (int i = 0; i < nominal.size(); i++)
                    if(nominal.get(i).sum > 0)
                        System.out.print(nominal.get(i).num + "[" + nominal.get(i).sum + "], ");

            } else {
                System.out.println("The exchange is unbroken");
            }

            System.out.println("\n\n");
        }
    }

    static boolean isNumeric(String string){
        try {
            Integer.parseInt(string);                                       //если ошибка при переводе в число, то будет исключение +надпись в консоль + вернёт false
            return true;
        } catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    static List<Integer> Sort(List<Integer> array){                         //сортировка

        Collections.sort(array, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2){
                return o1.compareTo(o2);
            }
        });

        return array;
    }

    static void Exchanges(int amount, int sum, List<Integer> array, List<Coin_quantity> array2, int arr_size){ //штука с рекурсией
        try {

            if(arr_size > 0) {                                              //если в массиве хотя бы 1 элемент, то работает

                if (amount >= array.get(arr_size) && array.get(arr_size) != 0) {//amount - сумма которую надо разменять, array.get… - получаем псследний и самый большой элемент массива + проверяем что это не 0
                    amount -= array.get(arr_size);                          //уменьшаем все число на номинал разменной монеты
                    sum += 1;                                               //+1 к количеству монет этого типа

                    Exchanges(amount, sum, array, array2, arr_size);        //рекурсия
                } else if (sum == 0) {
                    amount += array.get(arr_size+1);
                    array2.get(array2.size()-1).sum -= 1;

                    Exchanges(amount, sum, array, array2, arr_size);
                } else {                                                    //елси там 0 или номинал разменной монеты больше оставшейся суммы

                    Coin_quantity elem = new Coin_quantity(array.get(arr_size), sum); //создаём элемент класса коин что то там
                    array2.add(elem);                                       //закидываем его в массив
                    sum = 0;                                                //зануляем количество монет данного номинала

                    Exchanges(amount, sum, array, array2, --arr_size);        //рекурсия
                }
            }
        } catch (Exception e) {                                             //2 раза поймал ошибку, но ответ был в итоге правильный, хоть программа её упала
            System.out.println(e);
        }
    }

    static boolean crutch(List<Coin_quantity> array, int amount){
        int sum = 0;
        for(int i = 0; i < array.size(); i++)
            sum += array.get(i).sum * array.get(i).num;

        return (sum == amount);
    }
}

class Coin_quantity{ //класс с конструктором
    int num, sum; //num - номинал монеты
//Sum - количество таких монет для размена


    Coin_quantity(int a, int b){
        num = a;
        sum = b;
    }
}