import java.lang.reflect.Array;
import java.util.*;
import java.util.HashMap;

public class ParserImpl
{
    static Env env = new Env(null);
    HashMap<String, Object> curr_localdecl = new HashMap<>();
    HashMap<String, Object> curr_vardecl = new HashMap<>();
    HashMap<String, Object> curr_stmtlist = new HashMap<>();
    HashMap<String, Object> curr_param = new HashMap<>();
    HashMap<String, Object> curr_stmt = new HashMap<>();
    HashMap<String, Object> curr_func = null;
    public static String curr_input = new String();
    private static ArrayList<String> lst = new ArrayList<>();

    public Object program____decllist(Object p1) throws Exception {

        if (((Env)p1).Get("main") == null) {
            throw new Exception ("The program must have one main() function.");
        }
        if (!((HashMap<String, Object>)((Env)p1).Get("main")).get("typespec").equals("int")){
            throw new Exception ("The return type of main() function must be int.");
        }
        for (String funcName : ((Env)p1).GetKeys()){
            if (((Env)p1).Get(funcName).equals("int") || ((Env)p1).Get(funcName).equals("float") || ((Env)p1).Get(funcName).equals("bool")) continue;
            if (((Env)p1).Get_Typespec(funcName) != null && ((Env)p1).Get_Typespec(funcName).equals("struct")) continue;
            ArrayList<String> retList = get_return_types((HashMap<String, Object>)((Env)p1).Get(funcName));
            for (String retType : retList){
                if (!retType.equals(((Env)p1).Get_Typespec(funcName))){
                    String retVal = (String) ((HashMap<String, Object>)((HashMap<String, Object>)((HashMap<String, Object>)((Env)p1).Get(funcName)).get("stmtlist")).get("return")).get("value");
                    int[] retArr = get_Line_Column(retVal);
                    throw new Exception ("Function " + funcName + "() should return a \"" + ((Env)p1).Get_Typespec(funcName) + "\" value, not \"" + retType + "\" value."
                            + "\nError location is " + retArr[0] + ":" + retArr[1]);
                }
            }
        }
        env = env.prev;
        return env;
    }
    public Object decllist____decllist_decl(Object p1, Object p2) throws Exception {
        String key = getKey(p2);
        ((Env)p1).Put(key, ((HashMap<String, Object>)p2).get(key));
        curr_param = new HashMap<>();
        curr_localdecl = new HashMap<>();
        curr_stmtlist = new HashMap<>();
        curr_func = null;
        curr_input = new String();
        return p1;
    }
    public Object decllist____eps() throws Exception {
        Env env = new Env(this.env);
        this.env = env;
        return env;
    }
    public Object decl____vardecl(Object p1) throws Exception {
        curr_vardecl.put(getKey(p1),((HashMap<String, Object>)p1).get(getKey(p1)));
        Map<String, Object> map = new HashMap<>();
        map.put(getKey(p1),p1);
        return map;
    }
    public Object decl____strucdecl(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put((String) ((HashMap<String, Object>)p1).get("ident"), p1);
        return map;
    }
    public Object decl____fundecl(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put((String) ((HashMap<String, Object>)p1).get("ident"), p1);
        return map;
    }
    public Object vardecl____typespec_IDENT_SEMI(Object p1, Object p2) throws Exception {
        if (curr_vardecl.containsKey(p2)) {
            int[] retArr = get_Line_Column((String)p2);
            throw new Exception ("Variable " + p2 + " is already defined."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put((String)p2, p1);
        map.put("input", curr_input);
        return map;
    }
    public Object typespec____BOOL() throws Exception { return "bool"; }
    public Object typespec____INT() throws Exception {
        return "int";
    }
    public Object typespec____FLOAT() throws Exception { return "float"; }
    public Object typespec____STRUCT_IDENT(Object p1) throws Exception { return "struct " + p1; }
    public Object typespec____typespec_LBRACKET_RBRACKET(Object p1) throws Exception {
        if (p1.equals("int")) return "int[]";
        if (p1.equals("float")) return "float[]";
        return "bool[]";
    }
    public Object strucdecl____STRUCT_IDENT_LBRACE_localdecls_RBRACE_SEMI(Object p2, Object p4) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "struct");
        map.put("ident", p2);
        map.put("params", p4);
        map.put("input", curr_input);
        return map;
    }
    public Object fundecl____typespec_IDENT_LPAREN_params_RPAREN_LBRACE_localdecls_stmtlist_RBRACE(Object p1, Object p2, Object p4, Object p7, Object p8) throws Exception {
        if (!((HashMap<String, Object>)p8).containsKey("return")){
            ArrayList<String> lst = get_return_types((HashMap<String, Object>)p8);
            if (lst.size() == 0){
                int[] retArr = get_Line_Column("}");
                throw new Exception ("Function " + p2 + "() should return at least one \"" + p1 + "\" value."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("typespec", p1);
        map.put("ident", p2);
        map.put("params", p4);
        map.put("localdecls", p7);
        map.put("stmtlist", p8);
        map.put("input", curr_input);
        return map;
    }
    public Object params____paramlist(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        for (String str : ((HashMap<String, String>)p1).keySet()){
            map.put(str, ((HashMap<String, String>)p1).get(str));
        }
        return map;
    }
    public Object params____eps() throws Exception {
        Map<String, Object> map = new HashMap<>();
        return map;
    }
    public Object paramlist____paramlist_COMMA_param(Object p1, Object p3) throws Exception {
        ((HashMap<String, Object>) p1).putAll((HashMap<String, Object>) p3);
        return p1;
    }
    public Object paramlist____param(Object p1) throws Exception {
        return p1;
    }
    public Object param____typespec_IDENT(Object p1, Object p2) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put((String)p2, (String)p1);
        curr_param.put((String)p2, p1);
        return map;
    }
    public Object stmtlist____stmtlist_stmt(Object p1, Object p2) throws Exception {
        for (String key : ((HashMap<String, String>)p2).keySet()){
            ((HashMap<String, Object>)p1).put(key, ((HashMap<String, String>)p2).get(key));
        }
        return p1;
    }
    public Object stmtlist____eps() throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (curr_input.charAt(curr_input.length() - 1) == '{'){
            curr_stmt.put("curr_localdecl", curr_localdecl.clone());
            curr_stmt.put("curr_param", curr_param.clone());
            curr_stmt.put("curr_vardecl", curr_vardecl.clone());
            curr_stmt.put("curr_stmtlist", curr_stmtlist.clone());
            curr_localdecl = new HashMap<>();
            curr_param = new HashMap<>();
            curr_vardecl = new HashMap<>();
            curr_stmtlist = new HashMap<>();
        }
        return map;
    }
    public Object stmt____exprstmt_SEMI(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put((String) ((HashMap<String, Object>)p1).get("value"), ((HashMap<String, Object>)p1).get("typespec"));
        curr_stmtlist.put((String) ((HashMap<String, Object>)p1).get("value"), ((HashMap<String, Object>)p1).get("typespec"));
        return map;
    }
    public Object stmt____compoundstmt(Object p1) throws Exception {
        return p1;
    }
    public Object stmt____ifstmt(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("if", p1);
        return p1;
    }
    public Object stmt____whilestmt(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("while", p1);
        return p1;
    }
    public Object stmt____returnstmt_SEMI(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("return", p1);
        return map;
    }
    public Object stmt____printstmt_SEMI(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("print", p1);
        return map;
    }
    public Object stmt____SEMI() throws Exception {
        return new HashMap<>();
    }
    public Object exprstmt____IDENT_ASSIGN_exprstmt (Object p1, Object p3) throws Exception
    {
        String type1 = new String(), type2 = (String) ((HashMap<String, Object>)p3).get("typespec");
        if (curr_vardecl.containsKey(p1)) type1 = (String) curr_vardecl.get(p1);
        else if (curr_localdecl.containsKey(p1)) type1 = (String) curr_localdecl.get(p1);
        else if (curr_param.containsKey(p1)) type1 = (String) curr_param.get(p1);
        else if (curr_stmt.containsKey("curr_vardecl")){
            if (((HashMap<String, Object>)curr_stmt.get("curr_vardecl")).containsKey(p1)){
                type1 = (String) ((HashMap<String, Object>)curr_stmt.get("curr_vardecl")).get(p1);
            }
        }
        else if (curr_stmt.containsKey("curr_localdecl")){
            if (((HashMap<String, Object>)curr_stmt.get("curr_localdecl")).containsKey(p1)){
                type1 = (String) ((HashMap<String, Object>)curr_stmt.get("curr_localdecl")).get(p1);
            }
        }
        else if (curr_stmt.containsKey("curr_param")){
            if (((HashMap<String, Object>)curr_stmt.get("curr_param")).containsKey(p1)){
                type1 = (String) ((HashMap<String, Object>)curr_stmt.get("curr_param")).get(p1);
            }
        }

        if (!type1.equals(type2)){
            if (type1.equals("struct")) {
                int[] retArr = get_Line_Column((String) p1);
                throw new Exception ("\"" + type2 + "\" value cannot be assigned to \"" + type1 + " " + this.env.Get_Struct_Ident() + "\" variable " + p1 + "."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }
            if (type2.equals("struct")) {
                int[] retArr = get_Line_Column((String) p1);
                throw new Exception ("\"" + type2 + " " + this.env.Get_Struct_Ident() + "\" value cannot be assigned to \"" + type1 + "\" variable " + p1 + "."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }
            int[] retArr = get_Line_Column(p1 + " ");
            throw new Exception ("\"" + type2 + "\" value cannot be assigned to \"" + type1 + "\" variable " + p1 + "."
                    + "\nError location is " + retArr[0] + ":" + (retArr[1] + ((String) p1).length() + 1));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("value", p1 + " = " + ((HashMap<String, Object>)p3).get("value"));
        map.put("typespec", type1);
        return map;
    }
    public Object exprstmt____expr(Object p1) throws Exception
    {
        return p1;
    }
    public Object exprstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_exprstmt(Object p1, Object p3, Object p6) throws Exception {
        if (!((String)curr_localdecl.get((String)p1)).contains("[")){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Variable " + p1 + " must be an array type to use operator []."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (!((HashMap<String, Object>)p3).get("typespec").equals("int")){
            int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)p3).get("value"));
            throw new Exception ("Index of " + p1 + "[] must be an int value."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (((String)curr_localdecl.get(p1)).contains("[") && ((String)((HashMap<String, Object>)p6).get("typespec")).contains("[")){
            int idx = ((String) curr_localdecl.get(p1)).indexOf("[");
            String str = ((String) curr_localdecl.get(p1)).substring(0, idx);
            int[] retArr = get_Line_Column( "=");
            throw new Exception ("\"" + ((HashMap<String, Object>)p6).get("typespec") + "\" value cannot be assigned to \"" + str + "\" variable " + p1 + "[]."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, Object>)p6).get("typespec"));
        map.put("value", p1 + "[" + ((HashMap<String,String>)p3).get("value") + "] = " + ((HashMap<String,String>)p6).get("value"));
        return map;

    }
    public Object exprstmt____IDENT_DOT_IDENT_ASSIGN_exprstmt(Object p1, Object p3, Object p5) throws Exception {
        String type = new String();
        if (curr_localdecl.containsKey(p1)) type = (String) curr_localdecl.get(p1);
        else if (curr_param.containsKey(p1)) type = (String) curr_param.get(p1);
        else if (curr_vardecl.containsKey(p1)) type = (String) curr_vardecl.get(p1);
        else if (curr_stmt.containsKey("curr_localdecl") && ((HashMap<String,Object>)curr_stmt.get("curr_localdecl")).containsKey(p1)){
            type = (String) ((HashMap<String,Object>)curr_stmt.get("curr_localdecl")).get(p1);
        }
        else if (curr_stmt.containsKey("curr_param") && ((HashMap<String,Object>)curr_stmt.get("curr_param")).containsKey(p1)){
            type = (String) ((HashMap<String,Object>)curr_stmt.get("curr_param")).get(p1);
        }
        else if (curr_stmt.containsKey("curr_vardecl") && ((HashMap<String,Object>)curr_stmt.get("curr_vardecl")).containsKey(p1)){
            type = (String) ((HashMap<String,Object>)curr_stmt.get("curr_vardecl")).get(p1);
        }
        if (!type.contains("struct")) {
            int[] retArr = get_Line_Column(p1 + ".");
            throw new Exception ("Variable " + p1 + " must be a struct type to use field " + p3 + "."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }

        Map<String, Object> paramMap = this.env.Get_Struct_Parmas(type.substring(7));

        if (!paramMap.containsKey(p3)) {
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("\"" + type + "\" variable " + p1 + " does not have field " + p3 + "."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (!paramMap.get(p3).equals(((HashMap<String, Object>)p5).get("typespec"))){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("\"" + ((HashMap<String, Object>)p5).get("typespec") + "\" value cannot be assigned to \"" + paramMap.get(p3) + "\" variable " + p1 + "." + p3 + "."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("typespec", paramMap.get(p3));
        map.put("left", p1 + "." + p3);
        map.put("right", p5);
        return map;
    }
    public Object whilestmt____WHILE_LPAREN_expr_RPAREN_stmt(Object p3, Object p5) throws Exception {
        if (!((HashMap<String, String>)p3).get("typespec").equals("bool")){
            int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)p3).get("value"));
            throw new Exception ("\"while\" statement cannot use \"" + ((HashMap<String, String>)p3).get("typespec") + "\" value to check condition. Use bool value in while statement."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("expr", p3);
        map.put("stmt", p5);
        return map;
    }
    public Object compoundstmt____LBRACE_localdecls_stmtlist_RBRACE(Object p2, Object p3) {
        if (curr_stmt.size() != 0){
            if (curr_param.size() == 0 && curr_stmt.containsKey("curr_param")){
                curr_param = (HashMap<String, Object>)((HashMap<String,Object>)curr_stmt.get("curr_param")).clone();
            }
            curr_vardecl = (HashMap<String, Object>)((HashMap<String,Object>)curr_stmt.get("curr_vardecl")).clone();
            curr_localdecl = (HashMap<String, Object>)((HashMap<String,Object>)curr_stmt.get("curr_localdecl")).clone();
            curr_stmtlist = (HashMap<String, Object>)((HashMap<String,Object>)curr_stmt.get("curr_stmtlist")).clone();
            curr_stmt = new HashMap<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("localdecls", p2);
        map.put("stmtlist", p3);
        return map;
    }
    public Object localdecls____localdecls_localdecl(Object p1, Object p2) {
        Map<String, Object> map = (HashMap<String, Object>)p2;
        ((HashMap<String,Object>)p1).put((String) map.get("value"), map.get("typespec"));
        curr_localdecl.put((String) map.get("value"), map.get("typespec"));
        return p1;
    }
    public Object localdecls____eps() throws Exception {
        if (curr_func == null){
            String str = curr_input;
            str = str.replace("\n", "");
            if (str.contains("{")) str = str.substring(0, str.indexOf("{"));
            if (str.contains("(")) str = str.substring(0, str.indexOf("("));
            String[] split = str.split(" ");
            curr_func = new HashMap<>();
            curr_func.put(split[1], split[0]);
        }

        if (curr_input.charAt(curr_input.length() - 1) == '{'){
            if (curr_input.length() < 25){
                if ((curr_input.contains("while") || curr_input.contains("if") || curr_input.contains("else"))){
                    curr_stmt.put("curr_localdecl", curr_localdecl.clone());
                    curr_stmt.put("curr_vardecl", curr_vardecl.clone());
                    curr_stmt.put("curr_stmtlist", curr_stmtlist.clone());
                    curr_localdecl = new HashMap<>();
                    curr_vardecl = new HashMap<>();
                    curr_stmtlist = new HashMap<>();
                }
            }
            else{
                if (curr_input.substring(curr_input.length() - 25).contains("while")
                        || curr_input.substring(curr_input.length() - 25).contains("if")
                || curr_input.substring(curr_input.length() - 25).contains("else")){
                    curr_stmt.put("curr_localdecl", curr_localdecl.clone());
                    curr_stmt.put("curr_vardecl", curr_vardecl.clone());
                    curr_stmt.put("curr_stmtlist", curr_stmtlist.clone());
                    curr_localdecl = new HashMap<>();
                    curr_vardecl = new HashMap<>();
                    curr_stmtlist = new HashMap<>();
                }
            }
        }

        if (curr_localdecl.size() != 0 && curr_param.size() != 0 && curr_vardecl.size() != 0){
            if (curr_input.charAt(curr_input.length() - 1) == '{' && curr_input.charAt(curr_input.length() - 3) != ')'){
                curr_stmt.put("curr_localdecl", curr_localdecl.clone());
                curr_stmt.put("curr_param", curr_param.clone());
                curr_stmt.put("curr_vardecl", curr_vardecl.clone());
                curr_stmt.put("curr_stmtlist", curr_stmtlist.clone());
                curr_localdecl = new HashMap<>();
                curr_param = new HashMap<>();
                curr_vardecl = new HashMap<>();
                curr_stmtlist = new HashMap<>();
            }
        }
        Map<String, Object> map = new HashMap<>();
        return map;
    }
    public Object localdecl____typespec_IDENT_SEMI(Object p1, Object p2) throws Exception {
        if (curr_vardecl.containsKey(p2)) {
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Identifier " + p2 + " is already defined."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (curr_param.containsKey(p2)) {
            int[] retArr = get_Line_Column((String)p2);
            throw new Exception ("Identifier " + p2 + " is already defined."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (curr_localdecl.containsKey(p2)) {
            int[] retArr = get_Line_Column((String) p2);
            throw new Exception ("Identifier " + p2 + " is already defined."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", p1);
        map.put("value", p2);
        return map;
    }
    public Object ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(Object p3, Object p5, Object p7) throws Exception {
        if (!((HashMap<String, String>)p3).get("typespec").equals("bool")){
            int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)p3).get("value"));
            throw new Exception ("\"if\" statement cannot use \"" + ((HashMap<String, String>)p3).get("typespec") + "\" value to check condition. Use bool value in if statement."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("expr", p3);
        map.put("stmt1", p5);
        map.put("stmt2", p7);
        return map;
    }
    public Object returnstmt____RETURN_expr(Object p2) throws Exception {
        return p2;
    }
    public Object printstmt____PRINT_expr(Object p2) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, String>)p2).get("typespec"));
        if (((HashMap<String, String>)p2).get("value").contains("(")){
            map.put("value", ((HashMap<String, String>)p2).get("value").substring(1, ((HashMap<String, String>)p2).get("value").length()-1));
        }
        else{
            map.put("value", ((HashMap<String, String>)p2).get("value"));
        }
        return map;
    }
    public Object arglist____arglist_COMMA_expr(Object p1, Object p3) {
        ((ArrayList<Object>) p1).add(p3);
        return p1;
    }
    public Object arglist____expr(Object p1) {
        List<Object> lst = new ArrayList<>();
        lst.add(p1);
        return lst;
    }
    public Object args____arglist(Object p1) throws Exception {
        return p1;
    }
    public Object args____eps() { return env; }
    public Object expr____expr_OPADD_expr (Object p1, Object p3) throws Exception {
        if (((HashMap<String, Object>)p3).get("typespec") == null){
            if ((this.env).Get((String) ((HashMap<String, Object>)p3).get("value")) == null){
                int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)p3).get("value"));
                throw new Exception ("Undefine variable " + ((HashMap<String, Object>)p3).get("value") + " is used."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }else{
                if (((HashMap<String, Object>)(this.env).Get((String) ((HashMap<String, Object>)p3).get("value"))).containsKey("stmtlist")){
                    int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)p3).get("value"));
                    throw new Exception ("Function " + ((HashMap<String, Object>)p3).get("value") + "() cannot be used as a variable."
                            + "\nError location is " + retArr[0] + ":" + retArr[1]);
                }
            }
        }
        if (!((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("+");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " + " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, String>)p1).get("typespec"));
        map.put("value", ((HashMap<String, String>)p1).get("value") + "+" + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_OPSUB_expr(Object p1, Object p3) throws Exception {
        if (!((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("-");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " - " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, String>)p1).get("typespec"));
        map.put("value", ((HashMap<String, String>)p1).get("value") + "-" + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_OPMUL_expr(Object p1, Object p3) throws Exception {
        if (!((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("*");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " * " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, String>)p1).get("typespec"));
        map.put("value", ((HashMap<String, String>)p1).get("value") + "*" + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_OPDIV_expr(Object p1, Object p3) throws Exception {
        if (!((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " / " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed.");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, String>)p1).get("typespec"));
        map.put("value", ((HashMap<String, String>)p1).get("value") + "/" + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_OPMOD_expr(Object p1, Object p3) throws Exception {
        if (!env.Get((String)p1).equals(env.Get((String)p3))) throw new Exception ("Operation of \"" + env.Get((String)p1) + " % " + env.Get((String)p3) + "\" is not allowed.");
        env.Put((String)p1 + " % " + (String)p3, env.Get((String)p1));
        return p1 + " % " + p3;
    }
    public Object expr____expr_OPOR_expr(Object p1, Object p3) throws Exception {
        if (!((HashMap<String, String>)p1).get("typespec").equals("bool") || !((HashMap<String, String>)p3).get("typespec").equals("bool")){
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " or " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed.");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " or " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_OPAND_expr(Object p1, Object p3) throws Exception {
        if (!((HashMap<String, String>)p1).get("typespec").equals("bool") || !((HashMap<String, String>)p3).get("typespec").equals("bool")){
            int[] retArr = get_Line_Column("and");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " and " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " and " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____OPNOT_expr (Object p2) throws Exception {
        if (!((HashMap<String, String>)p2).get("typespec").equals("bool")){
            int[] retArr = get_Line_Column("not");
            throw new Exception ("Unary operation of \"not " + ((HashMap<String, String>)p2).get("typespec") + "\" is not allowed."
            + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", "not " + ((HashMap<String, String>)p2).get("value"));
        return map;
    }
    public Object expr____expr_RELOPEQ_expr(Object p1, Object p3) throws Exception
    {
        if (!((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("==");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " == " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " == " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_RELOPNE_expr(Object p1, Object p3) throws Exception
    {
        if (!((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("!=");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " != " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " != " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_RELOPLE_expr(Object p1, Object p3) throws Exception
    {
        if ((((HashMap<String, String>)p1).get("typespec").equals("bool") || ((HashMap<String, String>)p3).get("typespec").equals("bool"))
        || !((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("<=");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " <= " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " <= " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_RELOPLT_expr(Object p1, Object p3) throws Exception {
        if ((((HashMap<String, String>)p1).get("typespec").equals("bool") || ((HashMap<String, String>)p3).get("typespec").equals("bool"))
                || !((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column("<");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " < " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " < " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_RELOPGE_expr(Object p1, Object p3) throws Exception {
        if ((((HashMap<String, String>)p1).get("typespec").equals("bool") || ((HashMap<String, String>)p3).get("typespec").equals("bool"))
                || !((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column(">=");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " >= " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " >= " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____expr_RELOPGT_expr(Object p1, Object p3) throws Exception {
        if ((((HashMap<String, String>)p1).get("typespec").equals("bool") || ((HashMap<String, String>)p3).get("typespec").equals("bool"))
                || !((HashMap<String, String>)p1).get("typespec").equals(((HashMap<String, String>)p3).get("typespec"))){
            int[] retArr = get_Line_Column(">");
            throw new Exception ("Operation of \"" + ((HashMap<String, String>)p1).get("typespec") + " > " + ((HashMap<String, String>)p3).get("typespec") + "\" is not allowed."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "bool");
        map.put("value", ((HashMap<String, String>)p1).get("value") + " > " + ((HashMap<String, String>)p3).get("value"));
        return map;
    }
    public Object expr____LPAREN_expr_RPAREN(Object p2) throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("typespec", ((HashMap<String, String>)p2).get("typespec"));
        map.put("value", "(" + ((HashMap<String, String>)p2).get("value") + ")");
        return map;
    }
    public Object expr____IDENT(Object p1) throws Exception {
        if (curr_localdecl.get(p1) == null && curr_param.get(p1) == null && curr_vardecl.get(p1) == null){
            if ((curr_stmt.containsKey("curr_localdecl") && !((HashMap<String, Object>)curr_stmt.get("curr_localdecl")).containsKey(p1))
            && (curr_stmt.containsKey("curr_param") && !((HashMap<String, Object>)curr_stmt.get("curr_param")).containsKey(p1))
            && (curr_stmt.containsKey("curr_vardecl") && !((HashMap<String, Object>)curr_stmt.get("curr_vardecl")).containsKey(p1))){
                int[] retArr = get_Line_Column((String) p1);
                throw new Exception ("Undefine variable " + p1 + " is used."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("value", p1);
        if (curr_localdecl.containsKey(p1)) map.put("typespec", curr_localdecl.get(p1));
        else if (curr_param.containsKey(p1)) map.put("typespec", curr_param.get(p1));
        else if (curr_vardecl.containsKey(p1)) map.put("typespec", curr_vardecl.get(p1));
        else if (curr_stmt.containsKey("curr_localdecl") && ((HashMap<String, Object>)curr_stmt.get("curr_localdecl")).containsKey(p1)){
            map.put("typespec", ((HashMap<String, Object>)curr_stmt.get("curr_localdecl")).get(p1));
        }
        else if (curr_stmt.containsKey("curr_param") && ((HashMap<String, Object>)curr_stmt.get("curr_param")).containsKey(p1)){
            map.put("typespec", ((HashMap<String, Object>)curr_stmt.get("curr_param")).get(p1));
        }
        else if (curr_stmt.containsKey("curr_vardecl") && ((HashMap<String, Object>)curr_stmt.get("curr_vardecl")).containsKey(p1)){
            map.put("typespec", ((HashMap<String, Object>)curr_stmt.get("curr_vardecl")).get(p1));
        }
        return map;
    }
    public Object expr____BOOLLIT(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("value", (String)p1);
        map.put("typespec", "bool");
        return map;
    }
    public Object expr____INTLIT(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("value", (String)p1);
        map.put("typespec", "int");
        return map;
    }
    public Object expr____FLOATLIT(Object p1) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("value", (String)p1);
        map.put("typespec", "float");
        return map;
    }
    public Object expr____IDENT_LPAREN_args_RPAREN(Object p1, Object p3) throws Exception
    {
        if (curr_localdecl.get((String)p1) != null) {
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Identifier " + p1 + " is a \"" + curr_localdecl.get((String)p1) + "\" variable, not a function."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (this.env.Get((String)p1) == null){
            if (getKey(curr_func).equals(p1)){
                ArrayList<Object> lst1 = get_List(curr_param);
                ArrayList<Object> lst2 = (ArrayList<Object>)p3;
                for (int i = 0; i < lst1.size(); i++){
                    if (!((HashMap<String, Object>)lst1.get(i)).get("typespec").equals(((HashMap<String, String>)lst2.get(i)).get("typespec"))){
                        int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)((ArrayList<Object>)p3).get(i)).get("value"));
                        throw new Exception ((i + 1) + "th argument of function " + p1 + "() must be \"" + ((HashMap<String, String>)lst1.get(i)).get("typespec") + "\" type."
                                + "\nError location is " + retArr[0] + ":" + retArr[1]);
                    }
                }
                Map<String, Object> map = new HashMap<>();
                map.put("typespec", curr_func.get((String)p1));
                String newStr = "";
                newStr += p1 + "(";
                for (int i = 0; i < lst2.size(); i++){
                    if (i != lst2.size() - 1){
                        newStr += ((HashMap<String, String>)lst2.get(i)).get("value") + ", ";
                    }
                    else newStr += ((HashMap<String, String>)lst2.get(i)).get("value");
                }
                newStr += ")";
                map.put("value", newStr);
                return map;
            }
            else {
                int[] retArr = get_Line_Column((String) p1);
                throw new Exception ("Function " + p1 + "() is not defined."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }
        }

        ArrayList<Object> params =  Env.GetParams(this.env, (String)p1);
        ArrayList<Object> argms = (ArrayList<Object>)p3;
        if (params.size() != argms.size()) {
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Only " + params.size() + " arguments must be passed to function " + p1 + "()."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        for (int i = 0; i < params.size(); i++){
            if (!((HashMap<String, String>)params.get(i)).get("typespec").equals(((HashMap<String, String>)argms.get(i)).get("typespec"))){
                int[] retArr = get_Line_Column((String) ((HashMap<String, Object>)((ArrayList<Object>)p3).get(i)).get("value"));
                throw new Exception ((i + 1) + "th argument of function " + p1 + "() must be \"" + ((HashMap<String, String>)params.get(i)).get("typespec") + "\" type."
                        + "\nError location is " + retArr[0] + ":" + retArr[1]);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("ident", p1);
        map.put("typespec", ((HashMap<String, Object>)this.env.Get((String)p1)).get("typespec"));
        map.put("args", p3);
        return map;
    }
    public Object expr____IDENT_LBRACKET_expr_RBRACKET(Object p1, Object p3) throws Exception {
        if (curr_localdecl.get(p1) != null && !((String)curr_localdecl.get(p1)).contains("[")){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Variable " + p1 + " must be an array type to use operator []."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (curr_param.get(p1) != null && !((String)curr_param.get(p1)).contains("[")){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Variable " + p1 + " must be an array type to use operator []."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (!(((HashMap<String,String>)p3).get("typespec").equals("int"))){
            int[] retArr = get_Line_Column((((HashMap<String,String>)p3).get("value")));
            throw new Exception ("Index of " + p1 + "[] must be an int value."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        Map<String, Object> map = new HashMap<>();
        if (curr_localdecl.containsKey(p1)){
            if (curr_localdecl.get((String)p1).equals("int[]")) map.put("typespec", "int");
            if (curr_localdecl.get((String)p1).equals("float[]")) map.put("typespec", "float");
            if (curr_localdecl.get((String)p1).equals("bool[]")) map.put("typespec", "bool");
            int idx = ((String)curr_localdecl.get(p1)).indexOf("[");
            map.put("typespec", ((String) curr_localdecl.get(p1)).substring(0, idx));
            map.put("value", p1 + "[" + ((HashMap<String,String>)p3).get("value") + "]");
        }
        if (curr_param.containsKey(p1)){
            if (curr_param.get((String)p1).equals("int[]")) map.put("typespec", "int");
            if (curr_param.get((String)p1).equals("float[]")) map.put("typespec", "float");
            if (curr_param.get((String)p1).equals("bool[]")) map.put("typespec", "bool");
            int idx = ((String)curr_param.get(p1)).indexOf("[");
            map.put("typespec", ((String) curr_param.get(p1)).substring(0, idx));
            map.put("value", p1 + "[" + ((HashMap<String,String>)p3).get("value") + "]");
        }
        return map;
    }
    public Object expr____IDENT_DOT_SIZE(Object p1) throws Exception {
        if (curr_localdecl.get(p1) != null && !((String)curr_localdecl.get(p1)).contains("[")){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Variable " + p1 + " must be an array type to use \".size\""
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }
        if (curr_param.get(p1) != null && !((String)curr_param.get(p1)).contains("[")){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Variable " + p1 + " must be an array type to use \".size\""
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("typespec", "int");
        map.put("value", p1 + ".size");
        return map;
    }
    public Object expr____IDENT_DOT_IDENT  (Object p1, Object p3) throws Exception {
        String type = new String();
        if (curr_localdecl.containsKey(p1)) type = (String) curr_localdecl.get(p1);
        else if (curr_param.containsKey(p1)) type = (String) curr_param.get(p1);
        else if (curr_vardecl.containsKey(p1)) type = (String) curr_vardecl.get(p1);
        else if (curr_stmt.containsKey("curr_localdecl") && ((HashMap<String,Object>)curr_stmt.get("curr_localdecl")).containsKey(p1)){
            type = (String) ((HashMap<String,Object>)curr_stmt.get("curr_localdecl")).get(p1);
        }
        else if (curr_stmt.containsKey("curr_param") && ((HashMap<String,Object>)curr_stmt.get("curr_param")).containsKey(p1)){
            type = (String) ((HashMap<String,Object>)curr_stmt.get("curr_param")).get(p1);
        }
        else if (curr_stmt.containsKey("curr_vardecl") && ((HashMap<String,Object>)curr_stmt.get("curr_vardecl")).containsKey(p1)){
            type = (String) ((HashMap<String,Object>)curr_stmt.get("curr_vardecl")).get(p1);
        }

        if (!type.contains("struct")){
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("Variable " + p1 + " must be a struct type to use field " + p3 + "."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }

        String structName = this.env.Get_Struct_Ident();
        Map<String, Object> paramMap = this.env.Get_Struct_Parmas(structName);
        if (!paramMap.containsKey(p3)) {
            int[] retArr = get_Line_Column((String) p1);
            throw new Exception ("\"" + type + "\" variable " + p1 + " does not have field " + p3 + "."
                    + "\nError location is " + retArr[0] + ":" + retArr[1]);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("typespec", paramMap.get(p3));
        map.put("value", p1 + "." + p3);
        return map;
    }

    public static String getKey(Object p1){
        String[] arr = ((HashMap<String, Object>)p1).keySet().toArray(new String[((HashMap<String, Object>)p1).keySet().size()]);
        if (arr[0].equals("input")) return arr[1];
        return arr[0];
    }
    public static void get_recursion(HashMap<String, Object> map){
        if (map.containsKey("return")){
            map = (HashMap<String, Object>)map.get("return");
            lst.add((String) map.get("typespec"));
            return;
        }
        String[] arr = map.keySet().toArray(new String[map.keySet().size()]);
        for (int i = 0; i < arr.length; i++){
            if (arr[i].contains("stmt")) get_recursion((HashMap<String, Object>)map.get(arr[i]));
        }
    }
    public static ArrayList<String> get_return_types(HashMap<String, Object> map){
        lst = new ArrayList<>();
        get_recursion(map);
        ArrayList<String> retList = new ArrayList<>();
        for (int i = 0; i < lst.size(); i++){
            retList.add(lst.get(i));
        }
        return retList;
    }
    public static ArrayList<Object> get_List(HashMap<String, Object> map){
        ArrayList<Object> retList = new ArrayList<>();
        for (Object str : map.keySet()) {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("typespec", map.get(str));
            map2.put("value", str);
            retList.add(map2);
        }
        return retList;
    }
    public static int[] get_Line_Column(String cutStr){
        String str = get_All_Inputs();
        String testStr = str;
        int count = 0;
        while (testStr.length() > 0){
            if (!testStr.contains(cutStr)) break;
            testStr = testStr.substring(1);
            count++;
        }
        int lineNum = 1;
        int columnNum = 1;
        char[] cArr = str.toCharArray();
        for (int i = 0; i < count - 1; i++){
            if (cArr[i] == '\n'){
                lineNum++;
                columnNum = 1;
            }
            else columnNum++;
        }

        return new int[]{ lineNum, columnNum };
    }
    public static String get_All_Inputs(){
        String[] arr = env.GetKeys();
        String retStr = new String();
        for (String str : arr){
            if (((HashMap<String, Object>)env.Get(str)).containsKey("input")){
                retStr += ((HashMap<String, Object>)env.Get(str)).get("input");
            }
        }
        retStr += curr_input;
        curr_input = new String();
        return retStr;
    }
}
