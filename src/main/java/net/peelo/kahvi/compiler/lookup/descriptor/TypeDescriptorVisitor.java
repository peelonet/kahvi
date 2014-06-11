package net.peelo.kahvi.compiler.lookup.descriptor;

public interface TypeDescriptorVisitor<R, P>
{
    R visitArrayTypeDescriptor(ArrayTypeDescriptor td, P p);
    R visitClassTypeDescriptor(ClassTypeDescriptor td, P p);
    R visitPrimitiveTypeDescriptor(PrimitiveTypeDescriptor td, P p);
    R visitVoidTypeDescriptor(VoidTypeDescriptor td, P p);
}
