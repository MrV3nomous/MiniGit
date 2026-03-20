package minigit.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tree implements Serializable {
    private static final long serialVersionUID = 1L;
    public Map<String,String> entries = new HashMap<>();
}

