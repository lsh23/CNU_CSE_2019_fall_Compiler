import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MiniCPrintListener extends MiniCBaseListener {

    ParseTreeProperty<String> newTexts = new ParseTreeProperty<>();

//    boolean isBinaryOperation(MiniCParser.ExprContext ctx) {
//        return ctx.getChildCount() == 3 && ctx.getChild(1) != ctx.expr();
//    }
//
//
//    @Override
//    public void exitExpr(MiniCParser.ExprContext ctx) {
//        String s1 = null, s2 = null, op = null;
//
//        if(isBinaryOperation(ctx)){
//            s1 = newTexts.get(ctx.expr(0));
//            s2 = newTexts.get(ctx.expr(1));
//            op = ctx.getChild(1).getText();
//            newTexts.put(ctx, s1 + " " + op + " " + s2);
//        }
//    }
//
//    @Override
//    public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
//        super.enterFun_decl(ctx);
//    }


    @Override
    public void enterProgram(MiniCParser.ProgramContext ctx) {
        super.enterProgram(ctx);
    }

    @Override
    public void exitProgram(MiniCParser.ProgramContext ctx) {
        super.exitProgram(ctx);
    }

    @Override
    public void enterDecl(MiniCParser.DeclContext ctx) {
        super.enterDecl(ctx);
    }

    @Override
    public void exitDecl(MiniCParser.DeclContext ctx) {
        super.exitDecl(ctx);
    }

    @Override
    public void enterVar_decl(MiniCParser.Var_declContext ctx) {
        super.enterVar_decl(ctx);
    }

    @Override
    public void exitVar_decl(MiniCParser.Var_declContext ctx) {
        super.exitVar_decl(ctx);
    }

    @Override
    public void enterType_spec(MiniCParser.Type_specContext ctx) {
        super.enterType_spec(ctx);
    }

    @Override
    public void exitType_spec(MiniCParser.Type_specContext ctx) {
        super.exitType_spec(ctx);
    }

    @Override
    public void enterFun_decl(MiniCParser.Fun_declContext ctx) {
        super.enterFun_decl(ctx);
    }

    @Override
    public void exitFun_decl(MiniCParser.Fun_declContext ctx) {
        super.exitFun_decl(ctx);
    }

    @Override
    public void enterParam(MiniCParser.ParamContext ctx) {
        super.enterParam(ctx);
    }

    @Override
    public void exitParam(MiniCParser.ParamContext ctx) {
        super.exitParam(ctx);
    }

    @Override
    public void enterStmt(MiniCParser.StmtContext ctx) {
        super.enterStmt(ctx);
    }

    @Override
    public void exitStmt(MiniCParser.StmtContext ctx) {
        super.exitStmt(ctx);
    }

    @Override
    public void enterExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        super.enterExpr_stmt(ctx);
    }

    @Override
    public void exitExpr_stmt(MiniCParser.Expr_stmtContext ctx) {
        super.exitExpr_stmt(ctx);
    }

    @Override
    public void enterWhile_stmt(MiniCParser.While_stmtContext ctx) {
        super.enterWhile_stmt(ctx);
    }

    @Override
    public void exitWhile_stmt(MiniCParser.While_stmtContext ctx) {
        super.exitWhile_stmt(ctx);
    }

    @Override
    public void enterCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        super.enterCompound_stmt(ctx);
    }

    @Override
    public void exitCompound_stmt(MiniCParser.Compound_stmtContext ctx) {
        super.exitCompound_stmt(ctx);
    }

    @Override
    public void enterLocal_decl(MiniCParser.Local_declContext ctx) {
        super.enterLocal_decl(ctx);
    }

    @Override
    public void exitLocal_decl(MiniCParser.Local_declContext ctx) {
        super.exitLocal_decl(ctx);
    }

    @Override
    public void enterIf_stmt(MiniCParser.If_stmtContext ctx) {
        super.enterIf_stmt(ctx);
    }

    @Override
    public void exitIf_stmt(MiniCParser.If_stmtContext ctx) {
        super.exitIf_stmt(ctx);
    }

    @Override
    public void enterReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        super.enterReturn_stmt(ctx);
    }

    @Override
    public void exitReturn_stmt(MiniCParser.Return_stmtContext ctx) {
        super.exitReturn_stmt(ctx);
    }

    @Override
    public void enterExpr(MiniCParser.ExprContext ctx) {
        super.enterExpr(ctx);
    }

    @Override
    public void exitExpr(MiniCParser.ExprContext ctx) {
        super.exitExpr(ctx);
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
