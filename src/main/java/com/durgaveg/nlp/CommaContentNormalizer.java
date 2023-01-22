package com.durgaveg.nlp;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class CommaContentNormalizer {
	public static List<String> filterMerge(List<String> lines)
	{
		Stack<String> ret = new Stack<>();
		lines.forEach(line->{
			if(line.startsWith(",,,,")) 
				ret.push(",") ;
			else ret.push(line);
			});
		return ret.stream().collect(Collectors.toList());
	}
}
