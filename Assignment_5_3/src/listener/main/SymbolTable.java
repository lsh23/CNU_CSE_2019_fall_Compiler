package listener.main;


import generated.MiniCParser;
import generated.MiniCParser.Fun_declContext;
import generated.MiniCParser.Local_declContext;
import generated.MiniCParser.Var_declContext;

import java.util.HashMap;
import java.util.Map;

import static listener.main.BytecodeGenListenerHelper.getFunName;


public class SymbolTable {
	enum Type {
		INT, INTARRAY, VOID, ERROR, FLOAT
	}
	
	static public class VarInfo {
		Type type; 
		int id;
		double initVal;
		
		public VarInfo(Type type,  int id, int initVal) {
			this.type = type;
			this.id = id;
			this.initVal = initVal;
		}
		public VarInfo(Type type,  int id, float initVal) {
			this.type = type;
			this.id = id;
			this.initVal = initVal;
		}
		public VarInfo(Type type,  int id) {
			this.type = type;
			this.id = id;
			this.initVal = 0;
		}
	}
	
	static public class FInfo {
		public String sigStr;
	}
	
	private Map<String, VarInfo> _lsymtable = new HashMap<>();	// local v.
	private Map<String, VarInfo> _gsymtable = new HashMap<>();	// global v.
	private Map<String, FInfo> _fsymtable = new HashMap<>();	// function 
	
		
	private int _globalVarID = 0;
	private int _localVarID = 0;
	private int _labelID = 0;
	private int _tempVarID = 0;
	
	SymbolTable(){
		initFunDecl();
		initFunTable();
	}
	
	void initFunDecl(){		// at each func decl
		_localVarID = 0;
		_labelID = 0;
		_tempVarID = 32;		
	}
	
	void putLocalVar(String varname, Type type){
		//<Fill here>
		_lsymtable.put(varname,new VarInfo(type,_localVarID++));
	}
	
	void putGlobalVar(String varname, Type type){
		//<Fill here>
		_gsymtable.put(varname,new VarInfo(type,_globalVarID++));
	}
	
	void putLocalVarWithInitVal(String varname, Type type, int initVar){
		//<Fill here>
		_lsymtable.put(varname,new VarInfo(type,_localVarID++,initVar));
	}
	void putGlobalVarWithInitVal(String varname, Type type, int initVar){
		//<Fill here>
		_gsymtable.put(varname,new VarInfo(type,_globalVarID++,initVar));
	
	}

	void putLocalVarWithInitVal(String varname, Type type, float initVar){
		//<Fill here>
		_lsymtable.put(varname,new VarInfo(type,_localVarID++,initVar));
	}
	void putGlobalVarWithInitVal(String varname, Type type, float initVar){
		//<Fill here>
		_gsymtable.put(varname,new VarInfo(type,_globalVarID++,initVar));

	}



	
	void putParams(MiniCParser.ParamsContext params) {
		for ( MiniCParser.ParamContext param : params.param()){
			String paramName = param.IDENT().getText();
			Type paramType = null;
			if(param.type_spec().getText().toUpperCase().equals("INT")){
				paramType = Type.INT;
			}
			if(param.type_spec().getText().toUpperCase().equals("FLOAT")){
				paramType = Type.FLOAT;
			}
			putLocalVar(paramName,paramType);
		}
	}
	
	private void initFunTable() {
		FInfo printlninfo = new FInfo();
		printlninfo.sigStr = "java/io/PrintStream/println(I)V";
		
		FInfo maininfo = new FInfo();
		maininfo.sigStr = "main([Ljava/lang/String;)V";
		_fsymtable.put("_print", printlninfo);
		_fsymtable.put("main", maininfo);
	}
	
	public String getFunSpecStr(String fname) {		
		// <Fill here>
		return _fsymtable.get(fname).sigStr;
	}

	public String getPrintFunSpecStr(SymbolTable.Type type){
		if(type == Type.INT){
			return "java/io/PrintStream/println(I)V";
		}
		if(type == Type.FLOAT){
			return "java/io/PrintStream/println(F)V";
		}
		return "Transration Error : your input a non-exist type";
	}

	public String getFunSpecStr(Fun_declContext ctx) {
		// <Fill here>
		return _fsymtable.get(getFunName(ctx)).sigStr;
	}
	
	public String putFunSpecStr(Fun_declContext ctx) {
		String fname = getFunName(ctx);
		String argtype = "";	
		char rtype = ctx.getChild(0).getText().toUpperCase().charAt(0);;
		String res = "";
		
		// <Fill here>
		for ( MiniCParser.ParamContext param : ctx.params().param()){
			argtype +=param.type_spec().getText().toUpperCase().charAt(0);
		}

		res =  fname + "(" + argtype + ")" + rtype;
		
		FInfo finfo = new FInfo();
		finfo.sigStr = res;
		_fsymtable.put(fname, finfo);
		
		return res;
	}
	
	String getVarId(String name){
		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		if (lvar != null) {
			return String.valueOf(lvar.id);
		}

		VarInfo gvar = (VarInfo) _gsymtable.get(name);
		if (gvar != null) {
			return String.valueOf(gvar.id);
		}

		return Type.ERROR.toString();
	}
	
	Type getVarType(String name){
		VarInfo lvar = (VarInfo) _lsymtable.get(name);
		if (lvar != null) {
			return lvar.type;
		}
		
		VarInfo gvar = (VarInfo) _gsymtable.get(name);
		if (gvar != null) {
			return gvar.type;
		}
		
		return Type.ERROR;	
	}
	String newLabel() {
		return "label" + _labelID++;
	}
	
	String newTempVar() {
		String id = "";
		return id + _tempVarID--;
	}

	// global
	public String getVarId(Var_declContext ctx) {
		// <Fill here>
		String sname = "";
		sname += getVarId(ctx.IDENT().getText());
		return sname;
	}

	// local
	public String getVarId(Local_declContext ctx) {
		String sname = "";
		sname += getVarId(ctx.IDENT().getText());
		return sname;
	}
	
}
