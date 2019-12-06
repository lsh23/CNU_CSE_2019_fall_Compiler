package listener.main;

import generated.MiniCBaseListener;
import generated.MiniCParser;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MiniCPrintListener extends MiniCBaseListener {

    private int compund_depth = 0;
    //들여쓰기를 위한 변수
    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

    @Override
    public void enterProgram(MiniCParser.ProgramContext ctx) {
        super.enterProgram(ctx);
    }

    @Override
    public void exitProgram(MiniCParser.ProgramContext ctx) {
        // 모든 아래서 부터 받은 저장된 모든 decl에 대해서 출력.
        for(int i=0; i<ctx.getChildCount();i++) System.out.println(newTexts.get(ctx.decl(i)));
    }

    @Override
    public void enterDecl(MiniCParser.DeclContext ctx) {
        super.enterDecl(ctx);
    }

    @Override
    public void exitDecl(MiniCParser.DeclContext ctx) {
        // 밑에서 부터 올라온 해당 decl에 맞는 그대로 위로 올려준다.
        if(ctx.var_decl() != null){
            newTexts.put(ctx,newTexts.get(ctx.var_decl()));
        }
        if(ctx.fun_decl() != null){
            newTexts.put(ctx,newTexts.get(ctx.fun_decl()));
        }
    }

    @Override
    public void enterVar_decl(MiniCParser.Var_declContext ctx) {
        super.enterVar_decl(ctx);
    }

    @Override
    public void exitVar_decl(MiniCParser.Var_declContext ctx) {
        if(ctx.getChildCount() == 3){
            // type IDENT; 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String semicolon = ctx.getChild(2).getText();
            newTexts.put(ctx,type_spec + " " + ident + " " + semicolon);
        }
        if(ctx.getChildCount() == 5){
            // type IDENT = LITERAL ; 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String assignment = ctx.getChild(2).getText();
            String literal = ctx.getChild(3).getText();
            String semicolon = ctx.getChild(4).getText();
            newTexts.put(ctx, type_spec + " " + ident +" " + assignment + " " + literal + semicolon);
        }
        if(ctx.getChildCount() == 6){
            // type IDENT [ LITERAL ]; 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String open_braket = ctx.getChild(2).getText();
            String literal = ctx.getChild(3).getText();
            String close_braket = ctx.getChild(4).getText();
            String semicolon = ctx.getChild(5).getText();
            newTexts.put(ctx, type_spec + " " + ident +" " + open_braket +  literal + close_braket +semicolon);
        }
    }

    @Override
    public void enterType_spec(MiniCParser.Type_specContext ctx) {
        super.enterType_spec(ctx);
    }

    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        newTexts.put(ctx,ctx.getText());
    }

    @Override
    public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
        super.enterFun_decl(ctx);
    }

    @Override
    public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
        // typespec IDENT ( Params ) compound_stmt 인 경우
        String type_spec = newTexts.get(ctx.getChild(0));
        String ident = ctx.getChild(1).getText();
        String open_braket = ctx.getChild(2).getText();
        String params = newTexts.get(ctx.getChild(3));
        String close_braket = ctx.getChild(4).getText();
        String compound_stmt = newTexts.get(ctx.getChild(5));
        newTexts.put(ctx,type_spec + " " + ident + " " + open_braket +  params + close_braket + "\n" + compound_stmt);
    }

    @Override
    public void enterParams(MiniCParser.ParamsContext ctx) {
        super.enterParams(ctx);
    }

    @Override
    public void exitParams(MiniCParser.ParamsContext ctx) {
        if(ctx.getChildCount() == 0){
            // 파라미터가 없을때 단순 공백 삽입
            newTexts.put(ctx,"");
        }else{
            // 파라미터가 있을 때 ","으로 모든 파라미터 연결에서 위로 올린다.
            if(ctx.getChild(0).getText().equals("void")) newTexts.put(ctx,"void");
            else{
                String params = String.join(", " ,
                        ctx.param().stream().map( x -> newTexts.get(x) ).collect(Collectors.toList())
                );
                newTexts.put(ctx,params);
            }
        }

    }

    @Override
    public void enterParam(MiniCParser.ParamContext ctx) {
        super.enterParam(ctx);
    }

    @Override
    public void exitParam(MiniCParser.ParamContext ctx) {
        if(ctx.getChildCount()==2){
            // typespec IDENT 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            newTexts.put(ctx, type_spec + " " + ident);
        }
        if(ctx.getChildCount()==4){
            // type IDENT [] 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String open_braket = ctx.getChild(2).getText();
            String close_braket = ctx.getChild(3).getText();
            newTexts.put(ctx, type_spec + " " + ident + " " + open_braket + close_braket);
        }
    }

    @Override
    public void enterStmt(MiniCParser.StmtContext ctx) {
        super.enterStmt(ctx);
    }

    @Override
    public void exitStmt(MiniCParser.StmtContext ctx) {
        // 각각 stmt 의 해당 하는 텍스트를 그대로 위로 보낸다.
        if(ctx.expr_stmt()!=null){
            String expr_stmt = newTexts.get(ctx.expr_stmt());
            newTexts.put(ctx,expr_stmt);
        }
        if(ctx.compound_stmt()!=null){
            String compound_stmt = newTexts.get(ctx.compound_stmt());
            newTexts.put(ctx,compound_stmt);
        }
        if(ctx.if_stmt()!=null){
            String if_stmt = newTexts.get(ctx.if_stmt());
            newTexts.put(ctx,if_stmt);
        }
        if(ctx.while_stmt()!=null){
            String while_stmt = newTexts.get(ctx.while_stmt());
            newTexts.put(ctx,while_stmt);
        }
        if(ctx.return_stmt()!=null){
            String return_stmt = newTexts.get(ctx.return_stmt());
            newTexts.put(ctx,return_stmt);
        }
    }

    @Override
    public void enterExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        super.enterExpr_stmt(ctx);
    }

    @Override
    public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        // expr; 인 경우
        String expr = newTexts.get(ctx.expr());
        String semicolon = ctx.getChild(1).getText();
        newTexts.put(ctx, expr + semicolon);
    }

    @Override
    public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) {
        super.enterWhile_stmt(ctx);
    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
        // while ( expr ) stmt 의 경우
        String _while = ctx.getChild(0).getText();
        String open_braket = ctx.getChild(1).getText();
        String expr = newTexts.get(ctx.expr());
        String close_braket = ctx.getChild(3).getText();
        String stmt = newTexts.get(ctx.stmt());
        newTexts.put(ctx, _while + " " + open_braket + expr + close_braket + "\n" + stmt);
    }

    @Override
    public void enterCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        this.compund_depth++;
        //들여쓰기를 위해서 depth를 1씩 증가시킨다.
        super.enterCompound_stmt(ctx);
    }

    @Override
    public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        String open_braket = ctx.getChild(0).getText();
        String inner ="";
        for(int i=0; i<compund_depth-1;i++){
            inner += "....";
        }
        List<String> local_del_and_stmt = new ArrayList<>();
        for (MiniCParser.Local_declContext local_decl : ctx.local_decl()){
            local_del_and_stmt.add(newTexts.get(local_decl));
        }
        for(MiniCParser.StmtContext stmt : ctx.stmt()){
            local_del_and_stmt.add(newTexts.get(stmt));
        }
        String close_braket = ctx.getChild(ctx.getChildCount()-1).getText();
        String local_del_and_stmt_to_string = String.join("\n...."+inner,local_del_and_stmt);
        newTexts.put(ctx,inner + open_braket + "\n...."+inner + local_del_and_stmt_to_string + "\n"+inner + close_braket);

        compund_depth--;
        //{ local_decl* stmt*} 의 경우
        // depth의 대해서 ....을 추가 시키고 다시 exit 하기 전에 depth를 1 감소시킨다.



    }

    @Override
    public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
        super.enterLocal_decl(ctx);
    }

    @Override
    public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
        if(ctx.getChildCount() == 3){
            // typespec IDENT; 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String semicolon = ctx.getChild(2).getText();
            newTexts.put(ctx,type_spec + " " + ident + " " + semicolon);
        }
        if(ctx.getChildCount() == 5){
            // typespec IDENT = LITERAL; 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String assignment = ctx.getChild(2).getText();
            String literal = ctx.getChild(3).getText();
            String semicolon = ctx.getChild(4).getText();
            newTexts.put(ctx, type_spec + " " + ident +" " + assignment + " " + literal + semicolon);
        }
        if(ctx.getChildCount() == 6){
            // typespec IDENT [ LITERAL ]; 인 경우
            String type_spec = newTexts.get(ctx.getChild(0));
            String ident = ctx.getChild(1).getText();
            String open_braket = ctx.getChild(2).getText();
            String literal = ctx.getChild(3).getText();
            String close_braket = ctx.getChild(4).getText();
            String semicolon = ctx.getChild(5).getText();
            newTexts.put(ctx, type_spec + " " + ident +" " + open_braket + literal + close_braket + semicolon);
        }
    }

    @Override
    public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
        super.enterIf_stmt(ctx);
    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        if(ctx.getChildCount()==5){
            // if ( expr ) stmt 인 경우
            String _if = ctx.getChild(0).getText();
            String open_braket = ctx.getChild(1).getText();
            String expr = newTexts.get(ctx.expr());
            String close_braket = ctx.getChild(3).getText();
            String stmt = newTexts.get(ctx.stmt(0));
            newTexts.put(ctx, _if + " " + open_braket+expr+ close_braket + "\n" + stmt);
        }
        if(ctx.getChildCount()==7){
            // if ( expr ) stmt else stmt 인 경우
            String _if = ctx.getChild(0).getText();
            String open_braket = ctx.getChild(1).getText();
            String expr = newTexts.get(ctx.expr());
            String close_braket = ctx.getChild(3).getText();
            String stmt_1 = newTexts.get(ctx.stmt(0));
            String _else = ctx.getChild(5).getText();
            String stmt_2 = newTexts.get(ctx.stmt(1));
            newTexts.put(ctx, _if+" " + open_braket + expr + close_braket + "\n" + stmt_1 + "\n" + _else + "\n" + stmt_2);
        }
    }

    @Override
    public void enterReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        super.enterReturn_stmt(ctx);
    }

    @Override
    public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        if(ctx.getChildCount()==2){
            // return ; 인 경운
            String _return = ctx.getChild(0).getText();
            String semicolon = ctx.getChild(1).getText();
            newTexts.put(ctx, _return + semicolon);
        }else{
            // return expr ; 인경우
            String _return = ctx.getChild(0).getText();
            String expr = newTexts.get(ctx.expr());
            String semicolon = ctx.getChild(2).getText();
            newTexts.put(ctx, _return + " " + expr + semicolon);
        }
    }

    @Override
    public void enterExpr(MiniCParser.ExprContext ctx) {
        newTexts.put(ctx,ctx.getText());
    }

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx) {
        if(ctx.getChildCount() == 1){
            // LITERAL 또는 IDENT의 경우 바로 삽입
            newTexts.put(ctx,ctx.getText());
        }
        if(ctx.getChildCount() == 2){
                //++ expr 과 같은 꼴인 경우
            if(ctx.getChild(0) != ctx.expr(0)){
                String pre = ctx.getChild(0).getText();
                String expr = newTexts.get(ctx.expr(0));
                newTexts.put(ctx,pre + expr);
            }else{
                // expr ++ 과 같은 꼴인 경우
                String expr = newTexts.get(ctx.expr(0));
                String post = ctx.getChild(1).getText();
                newTexts.put(ctx,expr+post);
            }
        }
        if(ctx.getChildCount() == 3){
            if(ctx.getChild(1) != ctx.expr(0)){
                // expr 'op' expr 인 경우
                String expr_1 = newTexts.get(ctx.expr(0));
                String op = ctx.getChild(1).getText();
                String expr_2 = newTexts.get(ctx.expr(1));
                newTexts.put(ctx, expr_1 + op + expr_2);
            }
            if( ctx.getChild(1) == ctx.expr(0)){
                // ( expr ) 인 경우
                String open_braket = ctx.getChild(0).getText();
                String expr = newTexts.get(ctx.expr(0));
                String close_braket = ctx.getChild(2).getText();
                newTexts.put(ctx, open_braket + expr + close_braket);
            }
            if(ctx.getChild(2) == ctx.expr(0)){
                // IDENT = expr 인 경우
                String IDENT = ctx.getChild(0).getText();
                String assignment = ctx.getChild(1).getText();
                String expr = newTexts.get(ctx.expr(0));
                newTexts.put(ctx, IDENT + assignment + expr);
            }
        }
        if(ctx.getChildCount()==4){
            if(ctx.getChild(2) == ctx.expr(0)){
                // IDENT [ expr ] 인 경우
                String ident = ctx.getChild(0).getText();
                String open_braket = ctx.getChild(1).getText();
                String expr = newTexts.get(ctx.expr(0));
                String close_braket = ctx.getChild(3).getText();
                newTexts.put(ctx, ident + open_braket + expr + close_braket);
            }else{
                // IDENT ( args ) 인 경우
                String ident = ctx.getChild(0).getText();
                String open_braket = ctx.getChild(1).getText();
                String args = newTexts.get(ctx.args());
                String close_braket = ctx.getChild(3).getText();
                newTexts.put(ctx, ident + open_braket + args + close_braket);
            }
        }
        if(ctx.getChildCount()==6){
            //IDENT [ expr ] = expr 인 경우
            String ident = ctx.getChild(0).getText();
            String open_braket = ctx.getChild(1).getText();
            String expr_1 = newTexts.get(ctx.expr(0));
            String close_braket = ctx.getChild(3).getText();
            String asignment = ctx.getChild(4).getText();
            String expr_2 = newTexts.get(ctx.expr(1));
            newTexts.put(ctx, ident + open_braket + expr_1 + close_braket + " "+asignment+" "+expr_2);
        }
    }

    @Override
    public void enterArgs(MiniCParser.ArgsContext ctx) {
        super.enterArgs(ctx);
    }

    @Override
    public void exitArgs(MiniCParser.ArgsContext ctx) {
        super.exitArgs(ctx);
    }
}
