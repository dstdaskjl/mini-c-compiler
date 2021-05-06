import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Env
{
    private Map<String, Object> map;
    public Env prev;
    public Env(Env prev)
    {
        this.prev = prev;
        this.map = new HashMap<>();
    }
    public void Put(String name, Object value)
    {
        if (!map.containsKey(name)) map.put(name, value);
    }
    public Object Get(String name)
    {
        Map<String, Object> testMap = map;
        Env testPrev = prev;
        while (!testMap.containsKey(name)){
            if (testPrev != null) {
                testMap = testPrev.map;
                testPrev = testPrev.prev;
            }
            else break;
        }
        if (testMap.containsKey(name)) return testMap.get(name);
        return null;
    }
    public String[] GetKeys(){
        String[] arr = map.keySet().toArray(new String[map.size()]);
        return arr;
    }
    public String Get_Typespec(String key){
        return (String)((HashMap<String, Object>)map.get(key)).get("typespec");
    }
    public static ArrayList<Object> GetParams(Env env, String key){
        Map<String, Object> map = (HashMap<String, Object>)((HashMap<String, Object>)env.Get(key)).get("params");
        ArrayList<Object> retList = new ArrayList<>();
        for (Object str : map.keySet()) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("typespec", map.get(str));
            map2.put("value", str);
            retList.add(map2);
        }
        return retList;
    }
    public String Get_Struct_Ident(){
        String[] arr = GetKeys();
        for (String str : arr){
            Map<String, Object> map2 = (HashMap<String, Object>)map.get(str);
            if (map2.get("typespec").equals("struct")) return (String) map2.get("ident");
        }
        return null;
    }
    public HashMap<String, Object> Get_Struct_Parmas(String structName){
        return (HashMap<String, Object>)((HashMap<String, Object>)map.get(structName)).get("params");
    }
    public ArrayList<Object> GetList(){
        ArrayList<Object> lst = new ArrayList<>();
        for (String str : map.keySet()){
            Map<String, String> map = new HashMap<>();
            map.put("typespec", (String)this.map.get(str));
            map.put("value", str);
            lst.add(map);
        }
        return lst;
    }
}
