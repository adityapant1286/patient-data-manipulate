package functional;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Allows to filter elements in a stream using its property
 * when each element is an object.
 *
 * @param <F> Input type
 * @param <T> Output type

 * @version 1.0
 * @since 1.0
 * Date: 27-Feb-2021
 */
@FunctionalInterface
public interface DistinctPredicate<F, T> {

    /**
     * Accepts a type and returns a predicate which will be used
     * filtering a stream of data
     *
     * @param f Input
     * @return A {@link Predicate} of a specified type
     */
    Predicate<T> distinctByKey(F f);

    /**
     * Allows a stream objects to be filtered by object property.
     * This implementation utilises {@link Set#add(Object)}
     * which returns {@code true} when a {@code Set} did not already
     * contain the specified element. When the {@code add} method
     * returns {@code false}, the object will be filtered out from the
     * stream of objects.
     *
     * A static implementation of {@code distinctByKey} function.
     *
     * @param keyExtractor A function returning object property
     * @param <T> Type of object property
     * @return A {@link Predicate} with the result of {@link Set#add(Object)} method.
     * @see Set
     * @see Predicate
     *
     */
    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> visited = ConcurrentHashMap.newKeySet();
        return t -> visited.add(keyExtractor.apply(t));
    }
}
