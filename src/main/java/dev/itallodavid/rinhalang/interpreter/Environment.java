package dev.itallodavid.rinhalang.interpreter;

import dev.itallodavid.rinhalang.errors.UninitializedVariable;
import dev.itallodavid.rinhalang.language.kernel.Term;

import java.util.Map;

public interface Environment {
    public boolean contains(String key);
    public void put(String key, Term value);
    public void put(Map<String, Term> scope);
    public Term get(String key) throws UninitializedVariable;
    public Term remove(String key) throws UninitializedVariable;
    public Map<String, Term> environment();
    public Environment parent();
}
