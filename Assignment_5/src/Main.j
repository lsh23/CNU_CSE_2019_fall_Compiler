.class public Main
.super java/lang/Object
; strandard initializer
.method public <init>()V
aload_0
invokenonvirtual java/lang/Object/<init>()V
return
.end method
.method public static sum(I)I
.limit stack 32
.limit locals 32
iconst_0
istore_1
while: 
iload_0
ifle    done
iload_1
iload_0
iadd
istore_1
iload_0
iconst_1
isub
istore_0
goto    while
done:
iload_1
ireturn
.end method
.method public static main([Ljava/lang/String;)V
.limit stack 32
.limit locals 32
getstatic java/lang/System/out Ljava/io/PrintStream;
bipush  100
istore_1
iload_1
invokestatic Main/sum(I)I
invokevirtual java/io/PrintStream/println(I)V
return
.end method