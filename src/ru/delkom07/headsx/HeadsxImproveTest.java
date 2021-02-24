/**
 * Комментарии к заданию
 * Из задания не совсем ясны два момента:
 * 1) Насколько сильнО требование, что размеры массивов не должны совпадать?
 * 2) Необходима ли своя сортировка, или можно воспользоваться стандартными средствами?
 * Первое решено использованием множества (Set), второе - реализацией быстрой сортировки (QuickSort).
 * Если это было не важно, то кода могло бы быть в два раза меньше.
 * 
 * Также не указан тип (целое/с плавающей точкой) и диапазон - использован float в диапазоне от 0 до 1.
 * 
 * P.S. Т.к. требовалось написать "одну функцию", все реализовано в одном классе, статическими методами.
 */

package ru.delkom07.headsx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * Задача
 * На входе функция получает параметр n - натуральное число.
 * Необходимо сгенерировать n-массивов, заполнить их случайными числами, каждый массив имеет случайный размер.
 * Размеры массивов не должны совпадать. Далее необходимо отсортировать массивы. 
 * Массивы с четным порядковым номером отсортировать по возрастанию,
 * с нечетным порядковым номером - по убыванию. 
 * На выходе функция должна вернуть массив с отсортированными массивами.
 * @author KomyshevEG
 * 
 */
public class HeadsxImproveTest {
	private static final int MAX_ARRAYS_SIZE = 1000; // максимальный размер массивов
	private static Random sRandom = new Random(); // генератор случайных чисел

	/**
	 * main - для проверки функции. 
	 * Парсит входной аргумент, получает массив отсортированных массивов и печатает их в стандартный вывод.
	 * 
	 * @param args - ожидается одно натуральное число в качестве аргумента.
	 */
	public static void main(String[] args) {
		final int MAX_OUTPUT_STR_SIZE = 200; // максмальная длина строки на вывод
		try {
			if (0 != args.length) {
				int n = Integer.parseInt(args[0]);

				float[][] arrays = generateSortedArrays(n);

				Arrays.asList(arrays).forEach(array -> {
					String outArrStr = Arrays.toString(array);
					System.out.println(outArrStr.length() > MAX_OUTPUT_STR_SIZE ? outArrStr.subSequence(0, MAX_OUTPUT_STR_SIZE) + "..." : outArrStr);
				});
			} else {
				System.err.println("One argument required");
			}
		} catch (NumberFormatException e) {
			System.err.println("Enter a natural number");
		}
	}

	/**
	 * *** Функция из задания 
	 * Генерирует и сортирует (попеременно по возрастанию/убыванию) 
	 * n массивов случайной длины, заполненные случайными числами.
	 * 
	 * @param n - натуральное число.
	 * @return arrays - n отсортированных массивов.
	 */
	public static float[][] generateSortedArrays(int n) {
		float[][] arrays = new float[n][];

		// размеры массивов не должны совпадать - создадим множество неповторяющихся чисел
		HashSet<Integer> sizesSet = new HashSet<>();
		while (n != sizesSet.size()) {
			int rand = sRandom.nextInt(MAX_ARRAYS_SIZE) + 1;

			sizesSet.add(rand);
		}
		// и конвертируем это множество в массив
		Integer[] sizesSetArr = new Integer[n];
		sizesSetArr = sizesSet.toArray(sizesSetArr);

		// инициализация
		for (int i = 0; i < n; i++) {
			// int arraySize = random.nextInt(MAX_ARRAYS_SIZE); // так, возможно, были бы массивы одинаковой длины
			// xor
			int arraySize = sizesSetArr[i]; // так, уже точно не будут

			arrays[i] = new float[arraySize];

			for (int j = 0; j < arrays[i].length; j++) {
				arrays[i][j] = sRandom.nextFloat();
			}
		}

		// сортировка
		for (int i = 0; i < n; i++) {
			if (0 == i % 2) {
				//Arrays.sort(arrays[i]); // сортировка стандартным пакетом (JCF)
				// xor
				quickSort(arrays[i], 0, arrays[i].length - 1, true); // сортировка quickSort см. ниже

			} else {
				//Arrays.sort(arrays[i]); // сортировка стандартным пакетом (JCF)
				// invert(arrays[i]);
				// xor
				quickSort(arrays[i], 0, arrays[i].length - 1, false); // сортировка quickSort. см. ниже
			}
		}

		return arrays;
	}

	// invert требуется в случае сортировки стандартным пакетом (JCF)
	
	/**
	 * Инвертирует порядок элементов в массиве.
	 * 
	 * @param array - исходный массив.
	 */
	public static void invert(float[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			float temp = array[i];

			array[i] = array[array.length - 1 - i];
			array[array.length - 1 - i] = temp;
		}
	}

	// *** Далее - реализация QuickSort ***

	/**
	 * Сортирует массив. 
	 * QuickSort - один из самых быстрых известных универсальных алгоритмов сортировки массивов. 
	 * Реализован рекурсивно.
	 * 
	 * @param array       - исходный сортируемый массив.
	 * @param start     - индекс начала сортируемого под-массива.
	 * @param end       - индекс конца сортируемого под-массива.
	 * @param ascending - если true, сортировка по возрастанию, иначе по убыванию.
	 */
	public static void quickSort(float[] array, int start, int end, boolean ascending) {
		if (start < end) {
			int q = partition(array, start, end, ascending);
			quickSort(array, start, q, ascending);
			quickSort(array, q + 1, end, ascending);
		}
	}

	/**
	 * Разобьет и переупорядочит элементы под-массива.
	 * 
	 * @param array       - исходный массив.
	 * @param start     - индекс начала под-массива в исходном массиве.
	 * @param end       - индекс конца под-массива в исходном массиве.
	 * @param ascending - если true, сортировка по возрастанию, иначе по убыванию.
	 * @return - индекс опорного элемента.
	 */
	private static int partition(float[] array, int start, int end, boolean ascending) {
		float pivotVal = array[(end + start) / 2];

		int i = start;
		int j = end;
		while (i <= j) {
			if (ascending) {
				while (array[i] < pivotVal)
					i++;
				while (array[j] > pivotVal)
					j--;
			} else {
				while (array[i] > pivotVal)
					i++;
				while (array[j] < pivotVal)
					j--;
			}

			if (i >= j)
				break;

			swap(array, i++, j--);
		}

		return j;
	}

	/**
	 * Переставит два элемента массива местами.
	 * 
	 * @param array - массив.
	 * @param i - индекс первого переставляемого элемента в массиве.
	 * @param j - индекс второго переставляемого элемента в массиве.
	 */
	private static void swap(float[] array, int i, int j) {
		float temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

}
