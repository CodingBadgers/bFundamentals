package uk.codingbadgers.bFundamentals.utils;

import java.util.List;

import com.google.common.collect.ImmutableList.Builder;

public class CollectionUtils {

	public static <T> List<T> toImmutableList(List<T> list) {
		Builder<T> builder = new Builder<T>();
		builder.addAll(list);
		return builder.build();
	}
}
