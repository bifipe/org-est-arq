package Trabalho02;

import java.util.Comparator;

public class ComparaCEP implements Comparator<Endereco>
{
	public int compare(Endereco e1, Endereco e2)
    {
        return e1.getCep().compareTo(e2.getCep());
    }
}
