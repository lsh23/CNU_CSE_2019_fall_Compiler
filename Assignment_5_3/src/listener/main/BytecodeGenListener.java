package listener.main;

import generated.MiniCBaseListener;
import generated.MiniCParser;
import generated.MiniCParser.ParamsContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.List;

import static listener.main.BytecodeGenListenerHelper.*;
import static listener.main.SymbolTable.Type;

public class BytecodeGenListener extends MiniCBaseListener implements ParseTreeListener {
	ParseTreeProperty<String> newTexts = new ParseTreeProperty<String>();
	SymbolTable symbolTable = new SymbolTable();

	int tab = 0;
	int label = 0;

	// program	: decl+

	@Override
	public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
		symbolTable.initFunDecl();
		String fname = getFunName(ctx);
		ParamsContext params;
		if (fname.equals("main")) {
			symbolTable.putLocalVar("args", Type.INTARRAY);
		} else {
			symbolTable.putFunSpecStr(ctx);
			params = (MiniCParser.ParamsContext) ctx.getChild(3);
			symbolTable.putParams(params);
		}
	}


	// var_decl	: type_spec IDENT ';' | type_spec IDENT '=' LITERAL ';'|type_spec IDENT '[' LITERAL ']' ';'
	@Override
	public void enterVar_decl(MiniCParser.Var_declContext ctx) {
		String varName = ctx.IDENT().getText();

		if (isArrayDecl(ctx)) {
            if(ctx.getChild(0).getText().equals("int")){
                symbolTable.putGlobalVar(varName, Type.INTARRAY);
            }
            if(ctx.getChild(0).getText().equals("float")){
                symbolTable.putGlobalVar(varName, Type.FLOATARRAY);
            }
            if(ctx.getChild(0).getText().equals("double")){
                symbolTable.putGlobalVar(varName, Type.DOUBLE);
            }
            if(ctx.getChild(0).getText().equals("String")){
                symbolTable.putGlobalVar(varName, Type.STRINGARRAY);
            }
		}

		else if (isDeclWithInit(ctx)) {
			if(ctx.getChild(0).getText().equals("int")){
				symbolTable.putGlobalVarWithInitVal(varName, Type.INT, initVal(ctx));
			}
			if(ctx.getChild(0).getText().equals("float")){
				symbolTable.putGlobalVarWithInitVal(varName, Type.FLOAT, initVal(ctx));
			}
            if(ctx.getChild(0).getText().equals("double")){
                symbolTable.putGlobalVarWithInitVal(varName, Type.DOUBLE, initVal(ctx));
            }
			if(ctx.getChild(0).getText().equals("String")){
				symbolTable.putGlobalVarWithInitVal(varName, Type.STRING, initVal(ctx));
			}
		}
		else  { // simple decl
			if(ctx.type_spec().getText().equals("int")){
				symbolTable.putGlobalVar(varName, Type.INT);
			}
			if(ctx.type_spec().getText().equals("float")){
				symbolTable.putGlobalVar(varName, Type.FLOAT);
			}
            if(ctx.type_spec().getText().equals("double")){
                symbolTable.putGlobalVar(varName, Type.DOUBLE);
            }
			if(ctx.type_spec().getText().equals("String")){
				symbolTable.putGlobalVar(varName, Type.STRING);
			}
		}
	}


	@Override
	public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
		if (isArrayDecl(ctx)) {
            if (isArrayDecl(ctx)) {
                if(ctx.getChild(0).getText().equals("int")){
                    symbolTable.putLocalVar(getLocalVarName(ctx), Type.INTARRAY);
                }
                if(ctx.getChild(0).getText().equals("float")){
                    symbolTable.putLocalVar(getLocalVarName(ctx), Type.FLOATARRAY);
                }
                if(ctx.getChild(0).getText().equals("double")){
                    symbolTable.putLocalVar(getLocalVarName(ctx), Type.DOUBLEARRAY);
                }
                if(ctx.getChild(0).getText().equals("String")){
                    symbolTable.putLocalVar(getLocalVarName(ctx), Type.STRINGARRAY);
                }
            }
		}
		else if (isDeclWithInit(ctx)) {
			if(ctx.getChild(0).getText().equals("int")) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.INT, initVal(ctx));
			}
			if(ctx.getChild(0).getText().equals("float")) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.FLOAT, initValFloat(ctx));
			}
            if(ctx.getChild(0).getText().equals("double")) {
                symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.DOUBLE, initValDouble(ctx));
            }
			if(ctx.getChild(0).getText().equals("String")) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.STRING, initValString(ctx));
			}
		}
		else  { // simple decl
			if(ctx.type_spec().getText().equals("int")){
				symbolTable.putLocalVar(getLocalVarName(ctx), Type.INT);
			}
			if(ctx.type_spec().getText().equals("float")){
				symbolTable.putLocalVar(getLocalVarName(ctx), Type.FLOAT);
			}
            if(ctx.type_spec().getText().equals("double")){
                symbolTable.putLocalVar(getLocalVarName(ctx), Type.DOUBLE);
            }
			if(ctx.getChild(0).getText().equals("String")) {
				symbolTable.putLocalVar(getLocalVarName(ctx), Type.STRING);
			}
		}
	}


	@Override
	public void exitProgram(MiniCParser.ProgramContext ctx) {
		String classProlog = getFunProlog();

		String fun_decl = "", var_decl = "";

		for(int i = 0; i < ctx.getChildCount(); i++) {
			if(isFunDecl(ctx, i))
				fun_decl += newTexts.get(ctx.decl(i));
			else
				var_decl += newTexts.get(ctx.decl(i));
		}

		newTexts.put(ctx, classProlog + var_decl + fun_decl);

		System.out.println(newTexts.get(ctx));
	}


	// decl	: var_decl | fun_decl
	@Override
	public void exitDecl(MiniCParser.DeclContext ctx) {
		String decl = "";
		if(ctx.getChildCount() == 1)
		{
			if(ctx.var_decl() != null)				//var_decl
				decl += newTexts.get(ctx.var_decl());
			else							//fun_decl
				decl += newTexts.get(ctx.fun_decl());
		}
		newTexts.put(ctx, decl);
	}

	// stmt	: expr_stmt | compound_stmt | if_stmt | while_stmt | return_stmt
	@Override
	public void exitStmt(MiniCParser.StmtContext ctx) {
		String stmt = "";
		if(ctx.getChildCount() > 0)
		{
			if(ctx.expr_stmt() != null)				// expr_stmt
				stmt += newTexts.get(ctx.expr_stmt());
			else if(ctx.compound_stmt() != null)	// compound_stmt
				stmt += newTexts.get(ctx.compound_stmt());
			// <(0) Fill here>
			else if(ctx.if_stmt()!=null){
				stmt += newTexts.get(ctx.if_stmt());
			}
			else if(ctx.while_stmt()!=null){
				stmt += newTexts.get(ctx.while_stmt());
			}
			else if(ctx.return_stmt()!=null){
				stmt += newTexts.get(ctx.return_stmt());
			}
		}
		newTexts.put(ctx, stmt);
	}

	// expr_stmt	: expr ';'
	@Override
	public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
		String stmt = "";
		if(ctx.getChildCount() == 2)
		{
			stmt += newTexts.get(ctx.expr());	// expr
		}
		newTexts.put(ctx, stmt);
	}


	// while_stmt	: WHILE '(' expr ')' stmt
	@Override
	public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
		// <(1) Fill here!>
		String while_label = symbolTable.newLabel();
		String end_label = symbolTable.newLabel();
		String expr = newTexts.get(ctx.expr());
		String stmt = newTexts.get(ctx.stmt());
		// stack의 top에 있는 값 즉 while(x) 에서 x 에 해당 하는 값이
		// 0이면 end로 1이면 stmt로 가게 한다.
		String control = "ifeq  " + end_label +"\n" +
				          stmt+"goto "+while_label+"\n"+
				          end_label + ":\n";
		newTexts.put(ctx,while_label+":\n"+expr+"\n"+control);
	}


	@Override
	public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
		String func_head = funcHeader(ctx, getFunName(ctx));
		String compound_stmt = newTexts.get(ctx.getChild(5));
		boolean isvoid = isVoidF(ctx);
		String func_end = ".end method\n";
		if(isvoid){
			//void면
			newTexts.put(ctx,func_head+compound_stmt+"return\n"+func_end);
		}else{
			//return타입이 있으면
			newTexts.put(ctx,func_head+compound_stmt+func_end);
		}
	}


	private String funcHeader(MiniCParser.Fun_declContext ctx, String fname) {
		return ".method public static " + symbolTable.getFunSpecStr(fname) + "\n"
				+ ".limit stack " 	+ getStackSize(ctx) + "\n"
				+ ".limit locals " 	+ getLocalVarSize(ctx) + "\n";
	}



	@Override
	public void exitVar_decl(MiniCParser.Var_declContext ctx) {
		String varName = ctx.IDENT().getText();
		String varDecl = "";

		if (isDeclWithInit(ctx)) {
			varDecl += "putfield " + varName + "\n";
			// v. initialization => Later! skip now..:
		}
		newTexts.put(ctx, varDecl);
	}


	@Override
	public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
		String varDecl = "";
		if (isDeclWithInit(ctx)) {
			if(ctx.getChild(0).getText().equals("int")) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.INT, initVal(ctx));
				String vId = symbolTable.getVarId(ctx);
				varDecl += "ldc " + ctx.LITERAL().getText() + "\n"
						+ "istore " + vId + "\n";
			}
			if(ctx.getChild(0).getText().equals("float")) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.FLOAT, initValFloat(ctx));
				String vId = symbolTable.getVarId(ctx);
				varDecl += "ldc " + ctx.LITERAL().getText() + "\n"
						+ "fstore " + vId + "\n";
			}
            if(ctx.getChild(0).getText().equals("double")) {
                symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.DOUBLE, initValDouble(ctx));
                String vId = symbolTable.getVarId(ctx);
                varDecl += "ldc " + ctx.LITERAL().getText() + "\n"
                        + "dstore " + vId + "\n";
            }
			if(ctx.getChild(0).getText().equals("String")) {
				symbolTable.putLocalVarWithInitVal(getLocalVarName(ctx), Type.STRING, initValString(ctx));
				String vId = symbolTable.getVarId(ctx);
				varDecl += "ldc " + ctx.LITERAL().getText() + "\n"
						+ "astore " + vId + "\n";
			}
		}else if (isArrayDecl(ctx)) {
		    String arrayType ="";
		    if(ctx.getChild(0).getText().equals("int")) {
                arrayType = "int";
            }
            if(ctx.getChild(0).getText().equals("float")) {
                arrayType = "float";
            }
            if(ctx.getChild(0).getText().equals("double")) {
                arrayType = "double";
            }
            if(ctx.getChild(0).getText().equals("String")) {
                arrayType = "java/lang/String";
            }
		    int array_size = Integer.parseInt(ctx.getChild(3).getText());
            if(arrayType != "java/lang/String"){
                varDecl += "ldc "+array_size +"\n"+"newarray "+arrayType+"\n"+"astore "+symbolTable.getVarId(ctx.getChild(1).getText())+"\n";
            }
            else{
                varDecl += "ldc "+array_size +"\n"+"anewarray "+arrayType+"\n"+"astore "+symbolTable.getVarId(ctx.getChild(1).getText())+"\n";
                //String의 경우에는 anewarray로 처리해준다.
            }
        }
		newTexts.put(ctx, varDecl);
	}


	// compound_stmt	: '{' local_decl* stmt* '}'
	@Override
	public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
		// <(3) Fill here>
		List<String> local_del_and_stmt = new ArrayList<>();
		for (MiniCParser.Local_declContext local_decl : ctx.local_decl()){
			local_del_and_stmt.add(newTexts.get(local_decl));
		}
		for(MiniCParser.StmtContext stmt : ctx.stmt()){
			local_del_and_stmt.add(newTexts.get(stmt));
		}
		String local_del_and_stmt_to_string = String.join("",local_del_and_stmt);
		newTexts.put(ctx,local_del_and_stmt_to_string);

		// local_del이랑 stmt랑 합쳐준다.
	}

	// if_stmt	: IF '(' expr ')' stmt | IF '(' expr ')' stmt ELSE stmt;
	@Override
	public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
		String stmt = "";
		String condExpr= newTexts.get(ctx.expr());
		String thenStmt = newTexts.get(ctx.stmt(0));

		String lend = symbolTable.newLabel();
		String lelse = symbolTable.newLabel();


		if(noElse(ctx)) {
			stmt += condExpr + "\n"
					+ "ifeq " + lend + "\n"
					+ thenStmt + "\n"
					+ lend + ":"  + "\n";
		}
		else {
			String elseStmt = newTexts.get(ctx.stmt(1));
			stmt += condExpr + "\n"
					+ "ifeq " + lelse + "\n"
					+ thenStmt + "\n"
					+ "goto " + lend + "\n"
					+ lelse + ": " + elseStmt + "\n"
					+ lend + ":"  + "\n";
		}

		newTexts.put(ctx, stmt);
	}


	// return_stmt	: RETURN ';' | RETURN expr ';'
	@Override
	public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
		// <(4) Fill here>
		//return ; 인 경우
		if(ctx.getChildCount()==2) {
			newTexts.put(ctx, "return\n");
		}
		//return expr ; 인경우
		if(ctx.getChildCount()==3) {
			String expr = newTexts.get(ctx.expr());
//			if(expr.charAt(0) == 'i')
//			    newTexts.put(ctx, expr + "ireturn"+"\n");
//		    else
//                newTexts.put(ctx, expr + "freturn"+"\n");
            char type_prefix = expr.charAt(0);
            newTexts.put(ctx, expr + type_prefix+"return"+"\n");
		}
	}


	@Override
	public void exitExpr(MiniCParser.ExprContext ctx) {
		String expr = "";

		if(ctx.getChildCount() <= 0) {
			newTexts.put(ctx, "");
			return;
		}

		if(ctx.getChildCount() == 1) { // IDENT | LITERAL
			if(ctx.IDENT() != null) {
				String idName = ctx.IDENT().getText();
				if(symbolTable.getVarType(idName) == Type.INT) {
					expr += "iload " + symbolTable.getVarId(idName) + " \n";
				}
				if(symbolTable.getVarType(idName) == Type.FLOAT) {
					expr += "fload " + symbolTable.getVarId(idName) + " \n";
				}
                if(symbolTable.getVarType(idName) == Type.DOUBLE) {
                    expr += "dload " + symbolTable.getVarId(idName) + " \n";
                }
				if(symbolTable.getVarType(idName) == Type.STRING) {
					expr += "aload " + symbolTable.getVarId(idName) + " \n";
				}
				//else	// Type int array => Later! skip now..
				//	expr += "           lda " + symbolTable.get(ctx.IDENT().getText()).value + " \n";
			} else if (ctx.LITERAL() != null) {
				String literalStr = ctx.LITERAL().getText();
				expr += "ldc " + literalStr + " \n";
			}
		} else if(ctx.getChildCount() == 2) { // UnaryOperation
			expr = handleUnaryExpr(ctx, newTexts.get(ctx) + expr);
		}
		else if(ctx.getChildCount() == 3) {
			if(ctx.getChild(0).getText().equals("(")) { 		// '(' expr ')'
				expr = newTexts.get(ctx.expr(0));

			} else if(ctx.getChild(1).getText().equals("=")) { 	// IDENT '=' expr
				String idName = ctx.IDENT().getText();
				if(symbolTable.getVarType(idName) == Type.INT) {
					expr = newTexts.get(ctx.expr(0))
							+ "istore " + symbolTable.getVarId(ctx.IDENT().getText()) + " \n";
				}
				if(symbolTable.getVarType(idName) == Type.FLOAT) {
					expr = newTexts.get(ctx.expr(0))
							+ "fstore " + symbolTable.getVarId(ctx.IDENT().getText()) + " \n";
				}
                if(symbolTable.getVarType(idName) == Type.DOUBLE) {
                    expr = newTexts.get(ctx.expr(0))
                            + "dstore " + symbolTable.getVarId(ctx.IDENT().getText()) + " \n";
                }
				if(symbolTable.getVarType(idName) == Type.STRING) {
					String newStringBuilder = "new java/lang/StringBuilder\n" +
							"dup\n" +
							"invokespecial java/lang/StringBuilder/<init>()V\n";
					expr = newStringBuilder+newTexts.get(ctx.expr(0))
							+ "astore " + symbolTable.getVarId(ctx.IDENT().getText()) + " \n";
					// String의 경우에는 StringBuilder를 통해서 객체에 저장해주도록 한다.
				}

			} else { 											// binary operation
				expr = handleBinExpr(ctx, expr);
			}
		}
		// IDENT '(' args ')' |  IDENT '[' expr ']'
		else if(ctx.getChildCount() == 4) {
			if(ctx.args() != null){		// function calls
				expr = handleFunCall(ctx, expr);
			} else { // expr
				// Arrays: TODO
                String arrayName = ctx.getChild(0).getText();
                String type ="";
                if(symbolTable.getVarType(arrayName) == Type.INTARRAY) {
                    type = "i";
                }
                if(symbolTable.getVarType(arrayName) == Type.FLOATARRAY) {
                    type = "f";
                }
                if(symbolTable.getVarType(arrayName) == Type.DOUBLEARRAY) {
                    type = "d";
                }
                if(symbolTable.getVarType(arrayName) == Type.STRINGARRAY) {
                    type = "a";
                }
                expr = "aload "+symbolTable.getVarId(arrayName)+"\n"+newTexts.get(ctx.getChild(2))+type+"aload\n";
			}
		}
		// IDENT '[' expr ']' '=' expr
		else { // Arrays: TODO
		    String arrayName = ctx.getChild(0).getText();
		    String type ="";
            if(symbolTable.getVarType(arrayName) == Type.INTARRAY) {
                type = "ia";
            }
            if(symbolTable.getVarType(arrayName) == Type.FLOATARRAY) {
                type = "fa";
            }
            if(symbolTable.getVarType(arrayName) == Type.DOUBLEARRAY) {
                type = "da";
            }
            if(symbolTable.getVarType(arrayName) == Type.STRINGARRAY) {
                type = "aa";
            }
		    expr = "aload "+symbolTable.getVarId(ctx.getChild(0).getText())+"\n"+newTexts.get(ctx.getChild(2))+newTexts.get(ctx.getChild(5))+type+"store\n";
		}
		newTexts.put(ctx, expr);
	}


	private String handleUnaryExpr(MiniCParser.ExprContext ctx, String expr) {
		String idName = ctx.IDENT().getText();
		String type_prefix = "";
		if(symbolTable.getVarType(idName) == Type.INT) {
			type_prefix = "i";
		}
		if(symbolTable.getVarType(idName) == Type.FLOAT) {
			type_prefix = "f";
		}
        if(symbolTable.getVarType(idName) == Type.DOUBLE) {
            type_prefix = "d";
        }

		String l1 = symbolTable.newLabel();
		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		expr += newTexts.get(ctx.expr(0));
		switch(ctx.getChild(0).getText()) {
			case "-":
				expr += "            "+type_prefix+"neg \n"; break;
			case "--":
				expr += "ldc 1" + "\n"
						+ "isub" + "\n";
				break;
			case "++":
				expr += "ldc 1" + "\n"
						+ "iadd" + "\n";
				break;
			case "!":
				expr += "ifeq " + l2 + "\n"
						+ l1 + ": " + "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "ldc 1" + "\n"
						+ lend + ": " + "\n";
				break;
		}
		return expr;
	}


	private String handleBinExpr(MiniCParser.ExprContext ctx, String expr) {
		String idName = ctx.getChild(0).getText();
		String type_prefix = "";
		if(symbolTable.getVarType(idName) == Type.INT) {
			type_prefix = "i";
		}
		if(symbolTable.getVarType(idName) == Type.FLOAT) {
			type_prefix = "f";
		}
        if(symbolTable.getVarType(idName) == Type.DOUBLE) {
            type_prefix = "d";
        }
		if(symbolTable.getVarType(idName) == Type.STRING) {
			String append_bytecode = "invokevirtual java/lang/StringBuilder/append(Ljava/lang/String;)Ljava/lang/StringBuilder;";
			String id_1 = symbolTable.getVarId(ctx.getChild(0).getText());
			String id_2 = symbolTable.getVarId(ctx.getChild(2).getText());
			String end = "invokevirtual java/lang/StringBuilder/toString()Ljava/lang/String;";
			return "aload "+id_1+"\n"+append_bytecode+"\n"+"aload "+id_2+"\n"+append_bytecode+"\n"+end+"\n";
			//String의 경우에는 StringBuilder의 append를 이용해서 두 문자를 이어준다.
			//단이경우에는 String 타입의 경우 입력이 Stirng + String 만 들어온다고 가정 한 상황이다.
		}

		String l2 = symbolTable.newLabel();
		String lend = symbolTable.newLabel();

		expr += newTexts.get(ctx.expr(0));
		expr += newTexts.get(ctx.expr(1));

		switch (ctx.getChild(1).getText()) {
			case "*":
				expr += type_prefix+"mul \n"; break;
			case "/":
				expr += type_prefix+"div \n"; break;
			case "%":
				expr += type_prefix+"rem \n"; break;
			case "+":		// expr(0) expr(1) iadd
				expr += type_prefix+"add \n"; break;
			case "-":
				expr += type_prefix+"isub \n"; break;

			case "==":
				expr += type_prefix+"sub " + "\n"
						+ "ifeq "+ l2 +"\n"
						+ "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "\nldc 1" + "\n"
						+ lend + ": ";
				break;
			case "!=":
				expr += type_prefix+"sub " + "\n"
						+ "ifne "+ l2 +"\n"
						+ "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "\nldc 1" + "\n"
						+ lend + ": ";
				break;
			case "<=":
				// <(5) Fill here>
				// <= 면 1을 넣어주고 아니면 0을 넣는 작업
				expr += type_prefix+"sub " + "\n"
						+ "ifle "+ l2 +"\n"
						+ "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "\nldc 1" + "\n"
						+ lend + ": ";
				break;
			case "<":
				// <(6) Fill here>
				// < 면 1을 넣어주고 아니면 0을 넣는 작업
				expr += type_prefix+"sub " + "\n"
						+ "iflt "+ l2 +"\n"
						+ "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "\nldc 1" + "\n"
						+ lend + ": " ;
			break;

			case ">=":
				// <(7) Fill here>
				// >= 면 1을 넣어주고 아니면 0을 넣는 작업
				expr += type_prefix+"sub " + "\n"
						+ "ifge "+ l2 +"\n"
						+ "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "\nldc 1" + "\n"
						+ lend + ": ";
				break;


			case ">":
				// <(8) Fill here>
				// > 면 1을 넣어주고 아니면 0을 넣는 작업
				expr += type_prefix+"sub " + "\n"
						+ "ifgt "+ l2 +"\n"
						+ "ldc 0" + "\n"
						+ "goto " + lend + "\n"
						+ l2 + ": " + "\nldc 1" + "\n"
						+ lend + ": ";
				break;

			case "and":
				expr +=  "ifne "+ lend + "\n"
						+ "pop" + "\n" + "ldc 0" + "\n"
						+ lend + ": "; break;
			case "or":
				// <(9) Fill here>
				expr +=  "ifeq "+ lend + "\n"
						+ "pop" + "\n" + "ldc 1" + "\n"
						+ lend + ": "; break;

		}
		return expr;
	}
	private String handleFunCall(MiniCParser.ExprContext ctx, String expr) {
		String fname = getFunName(ctx);

		if (fname.equals("_print")) {		// System.out.println
		    String calle_func_name = ctx.args().expr(0).IDENT().toString();
            String type = symbolTable.getFunSpecStr(calle_func_name);
            Type print_type = null;
            if(type != null){
                if(type.charAt(type.length()-1) == 'I'){
                    print_type = Type.INT;
                }
                if(type.charAt(type.length()-1) == 'F' ) {
                    print_type = Type.FLOAT;
                }
                if(type.charAt(type.length()-1) == 'D' ) {
                    print_type = Type.DOUBLE;
                }
				if(type.charAt(type.length()-1) == ';' ) {
					print_type = Type.STRING;
				}
            }
            else{
                Type t = symbolTable.getVarType(calle_func_name);
                if(t == Type.INT || t == Type.INTARRAY && ctx.args().expr(0).children.size() == 4){
                    print_type = Type.INT;
                }
                else if(t == Type.FLOAT || t == Type.FLOATARRAY && ctx.args().expr(0).children.size() == 4) {
                    print_type = Type.FLOAT;
                }
                else if(t == Type.DOUBLE || t == Type.DOUBLEARRAY && ctx.args().expr(0).children.size() == 4) {
                    print_type = Type.DOUBLE;
                }
				else if(t == Type.STRING || t == Type.STRINGARRAY && ctx.args().expr(0).children.size() == 4) {
					print_type = Type.STRING;
				}else if( t == Type.INTARRAY ){
				    print_type = Type.INTARRAY;
                }else if( t == Type.FLOATARRAY ){
                    print_type = Type.FLOATARRAY;
                }else if( t == Type.DOUBLEARRAY ){
                    print_type = Type.DOUBLEARRAY;
                }else if( t == Type.STRINGARRAY ){
                    print_type = Type.STRINGARRAY;
                }
            }
            if(print_type == Type.INT || print_type == Type.FLOAT || print_type == Type.DOUBLE || print_type == Type.STRING) {
				expr = "getstatic java/lang/System/out Ljava/io/PrintStream; " + "\n"
						+ newTexts.get(ctx.args())
						+ symbolTable.getPrintFunSpecStr(print_type) + "\n";
			}else{
				expr = "getstatic java/lang/System/out Ljava/io/PrintStream; " + "\n"
						+ newTexts.get(ctx.args())
						+ "aload " + symbolTable.getVarId(ctx.getChild(2).getText())+"\n"
						+ symbolTable.getPrintFunSpecStr(print_type) + "\n";
				//배열의 경우
			}
		} else {
			expr = newTexts.get(ctx.args())
					+ "invokestatic " + getCurrentClassName()+ "/" + symbolTable.getFunSpecStr(fname) + "\n";
		}

		return expr;

	}

	// args	: expr (',' expr)* | ;
	@Override
	public void exitArgs(MiniCParser.ArgsContext ctx) {

		String argsStr = "";

		for (int i=0; i < ctx.expr().size() ; i++) {
			argsStr += newTexts.get(ctx.expr(i)) ;
		}
		newTexts.put(ctx, argsStr);
	}

}
