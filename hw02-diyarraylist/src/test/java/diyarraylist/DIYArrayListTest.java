package diyarraylist;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест DIYArrayListTest")
class DIYArrayListTest {
    private static final int LIST_SIZE = 42;

    @Test
    @DisplayName("должен проверить Collections.addAll(Collection<? super T> c, T... elements)")
    public void shouldAddAll() {
        List<String> strings = new DIYArrayList<>();
        String elementOne = "one";
        String elementTwo = "two";
        String elementThree = "three";

        Collections.addAll(strings, elementOne, elementTwo, elementThree);
        assertTrue(strings.contains(elementOne));
        assertTrue(strings.contains(elementTwo));
        assertTrue(strings.contains(elementThree));
    }

    @Test
    @DisplayName("должен проверить Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)")
    public void shouldCopyList() {
        List<String> src = new DIYArrayList<>(LIST_SIZE);
        for (int i = 0; i < LIST_SIZE; i++) {
            src.add(String.valueOf(i));
        }

        List<String> dest = new DIYArrayList<>(src.size());
        for (int i = 0; i < src.size(); i++) {
            dest.add(null);
        }

        Collections.copy(dest, src);
        for (int i = 0; i < src.size(); i++) {
            assertEquals(src.get(i), dest.get(i));
        }
    }

    @Test
    @DisplayName("должен проверить Collections.static <T> void sort(List<T> list, Comparator<? super T> c)")
    public void shouldSortList() {
        List<String> strings = new DIYArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            strings.add(String.valueOf(i));
        }

        Collections.sort(strings, Comparator.naturalOrder());
        assertThat(strings).isSorted();

        Collections.sort(strings, Comparator.reverseOrder());
        assertThat(strings).isSortedAccordingTo(Comparator.reverseOrder());

        List<String> otherStrings = new DIYArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            byte[] tmp = new byte[i + 1];
            new Random().nextBytes(tmp);
            otherStrings.add(new String(tmp, Charset.forName(StandardCharsets.UTF_8.name())));
        }

        Comparator<String> comparator = Comparator.comparing(String::length);
        Collections.sort(otherStrings, comparator);
        assertThat(otherStrings).isSortedAccordingTo(comparator);
    }

    @Test
    @DisplayName("должен венуть корректный размер List'а")
    void shouldReturnSize() {
        List<String> strings = new DIYArrayList<>(LIST_SIZE);
        for (int i = 0; i < LIST_SIZE; i++) {
            strings.add("test");
        }
        assertEquals(LIST_SIZE, strings.size());

        List<String> emptyList = new DIYArrayList<>();
        assertEquals(0, emptyList.size());

        List<String> someList = new DIYArrayList<>(LIST_SIZE);
        assertEquals(0, someList.size());
    }

    @Test
    @DisplayName("должен проверять пустоту List'а")
    void shouldCheckListIsEmpty() {
        List<String> strings = new DIYArrayList<>(LIST_SIZE);
        assertFalse(strings.isEmpty());

        List<String> emptyArray = new DIYArrayList<>();
        assertTrue(emptyArray.isEmpty());
    }

    @Test
    @DisplayName("должен проверять наличие элементов в List'е")
    void shouldCheckListContainsElement() {
        List<String> strings = new DIYArrayList<>();
        String testString = "test";
        strings.add(testString);
        assertTrue(strings.contains(testString));

        String wrongString = "wrong";
        assertFalse(strings.contains(wrongString));
    }

    @Test
    @DisplayName("должен проверить next() hasNext() итератора")
    void shouldCheckIterator() {
        String elementValue = "someString";
        List<String> strings = new DIYArrayList<>(LIST_SIZE);
        Iterator<String> itr = strings.iterator();

        assertFalse(itr.hasNext());
        assertThrows(NoSuchElementException.class, itr::next);

        for (int i = 0; i < LIST_SIZE; i++) {
            strings.add(elementValue);
        }
        for (int i = 0; i < LIST_SIZE; i++) {
            assertTrue(itr.hasNext());
            assertEquals(elementValue, itr.next());
        }
        assertFalse(itr.hasNext());
        assertThrows(NoSuchElementException.class, itr::next);
    }

    @Test
    @DisplayName("должен добавить элемент в List")
    void shouldAddElement() {
        String newElement = "new";
        List<String> strings = new DIYArrayList<>();

        assertTrue(strings.isEmpty());
        assertTrue(strings.add(newElement));
        assertEquals(1, strings.size());
        assertTrue(strings.contains(newElement));

        strings = new DIYArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            strings.add(String.valueOf(i));
        }
        assertFalse(strings.isEmpty());
        assertTrue(strings.add(newElement));
        assertEquals(LIST_SIZE + 1, strings.size());
        assertTrue(strings.contains(newElement));
    }

    @Test
    @DisplayName("должен возвращять элементы List'а по индексу")
    void shouldGetByIndex() {
        int zeroIdx = 0;
        int incorrectIdx = LIST_SIZE * 2;
        int negativeIncorrectIdx = -LIST_SIZE;
        String newElement = "new";
        List<String> strings = new DIYArrayList<>();
        strings.add(newElement);
        assertEquals(newElement, strings.get(zeroIdx));
        assertThrows(IndexOutOfBoundsException.class, () -> strings.get(incorrectIdx));
        assertThrows(IndexOutOfBoundsException.class, () -> strings.get(negativeIncorrectIdx));

        List<String> otherStrings = new DIYArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            otherStrings.add("test");
        }
        otherStrings.add(newElement);

        assertEquals(newElement, strings.get(zeroIdx));
        assertNotNull(otherStrings.get(LIST_SIZE));
        assertThrows(IndexOutOfBoundsException.class, () -> strings.get(incorrectIdx));
        assertThrows(IndexOutOfBoundsException.class, () -> strings.get(negativeIncorrectIdx));
    }

    @Test
    @DisplayName("должен проверить вставку по индексу")
    void shouldSetElement() {
        List<String> strings = new DIYArrayList<>(LIST_SIZE);
        for (int i = 0; i < LIST_SIZE; i++) {
            strings.add(String.valueOf(i));
        }
        int newElementIndex = 12;
        String newElement = "test";

        strings.set(newElementIndex, newElement);
        assertEquals(newElement, strings.get(newElementIndex));
    }

    @Test
    @DisplayName("должен проверить set(E) ListIterator`a")
    void shouldTestListIterator() {
        String elementValue = "some String";
        String newElementValue = "new String";
        List<String> strings = new DIYArrayList<>();
        ListIterator<String> listItr = strings.listIterator();

        for (int i = 0; i < LIST_SIZE; i++) {
            strings.add(elementValue + i);
        }

        while (listItr.hasNext()) {
            listItr.next();
            listItr.set(newElementValue);
        }

        Condition<String> condition = new Condition<>(e -> e.equals(newElementValue), "newElementValue");
        assertThat(strings).haveExactly(LIST_SIZE, condition);
    }
}