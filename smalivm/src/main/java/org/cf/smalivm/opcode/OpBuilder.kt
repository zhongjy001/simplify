package org.cf.smalivm.opcode

import org.cf.smalivm.configuration.Configuration
import org.cf.smalivm.dex.SmaliClassLoader
import org.cf.smalivm.type.ClassManager
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.builder.MethodLocation
import org.slf4j.LoggerFactory

class OpBuilder(
    private val addressToLocation: Map<Int, MethodLocation>,
    private val classManager: ClassManager,
    private val classLoader: SmaliClassLoader,
    private val configuration: Configuration,
) {
    fun build(location: MethodLocation): Op {
        val instruction = location.instruction!!
        val opcode = instruction.opcode
        val opFactory = getOpFactory(opcode)
        return opFactory.build(location, addressToLocation, classManager, classLoader, configuration)
    }

    companion object {
        private val log = LoggerFactory.getLogger(OpBuilder::class.java.simpleName)

        private fun getOpFactory(op: Opcode): OpFactory {
            return when (op) {
                Opcode.ADD_DOUBLE, Opcode.ADD_DOUBLE_2ADDR, Opcode.ADD_FLOAT, Opcode.ADD_FLOAT_2ADDR, Opcode.ADD_INT, Opcode.ADD_INT_2ADDR, Opcode.ADD_INT_LIT16, Opcode.ADD_INT_LIT8, Opcode.ADD_LONG, Opcode.ADD_LONG_2ADDR, Opcode.AND_INT, Opcode.AND_INT_2ADDR, Opcode.AND_INT_LIT16, Opcode.AND_INT_LIT8, Opcode.AND_LONG, Opcode.AND_LONG_2ADDR, Opcode.DIV_DOUBLE, Opcode.DIV_DOUBLE_2ADDR, Opcode.DIV_FLOAT, Opcode.DIV_FLOAT_2ADDR, Opcode.DIV_INT, Opcode.DIV_INT_2ADDR, Opcode.DIV_INT_LIT16, Opcode.DIV_INT_LIT8, Opcode.DIV_LONG, Opcode.DIV_LONG_2ADDR, Opcode.MUL_DOUBLE, Opcode.MUL_DOUBLE_2ADDR, Opcode.MUL_FLOAT, Opcode.MUL_FLOAT_2ADDR, Opcode.MUL_INT, Opcode.MUL_INT_2ADDR, Opcode.MUL_INT_LIT16, Opcode.MUL_INT_LIT8, Opcode.MUL_LONG, Opcode.MUL_LONG_2ADDR, Opcode.OR_INT, Opcode.OR_INT_2ADDR, Opcode.OR_INT_LIT16, Opcode.OR_INT_LIT8, Opcode.OR_LONG, Opcode.OR_LONG_2ADDR, Opcode.REM_DOUBLE, Opcode.REM_DOUBLE_2ADDR, Opcode.REM_FLOAT, Opcode.REM_FLOAT_2ADDR, Opcode.REM_INT, Opcode.REM_INT_2ADDR, Opcode.REM_INT_LIT16, Opcode.REM_INT_LIT8, Opcode.REM_LONG, Opcode.REM_LONG_2ADDR, Opcode.RSUB_INT, Opcode.RSUB_INT_LIT8, Opcode.SHL_INT, Opcode.SHL_INT_2ADDR, Opcode.SHL_INT_LIT8, Opcode.SHL_LONG, Opcode.SHL_LONG_2ADDR, Opcode.SHR_INT, Opcode.SHR_INT_2ADDR, Opcode.SHR_INT_LIT8, Opcode.SHR_LONG, Opcode.SHR_LONG_2ADDR, Opcode.SUB_DOUBLE, Opcode.SUB_DOUBLE_2ADDR, Opcode.SUB_FLOAT, Opcode.SUB_FLOAT_2ADDR, Opcode.SUB_INT, Opcode.SUB_INT_2ADDR, Opcode.SUB_LONG, Opcode.SUB_LONG_2ADDR, Opcode.USHR_INT, Opcode.USHR_INT_2ADDR, Opcode.USHR_INT_LIT8, Opcode.USHR_LONG, Opcode.USHR_LONG_2ADDR, Opcode.XOR_INT, Opcode.XOR_INT_2ADDR, Opcode.XOR_INT_LIT16, Opcode.XOR_INT_LIT8, Opcode.XOR_LONG, Opcode.XOR_LONG_2ADDR -> BinaryMathOp
                Opcode.AGET, Opcode.AGET_BOOLEAN, Opcode.AGET_BYTE, Opcode.AGET_CHAR, Opcode.AGET_OBJECT, Opcode.AGET_SHORT, Opcode.AGET_WIDE -> AGetOp
                Opcode.APUT, Opcode.APUT_BOOLEAN, Opcode.APUT_BYTE, Opcode.APUT_CHAR, Opcode.APUT_OBJECT, Opcode.APUT_SHORT, Opcode.APUT_WIDE -> APutOp
                Opcode.ARRAY_LENGTH -> ArrayLengthOp
                Opcode.ARRAY_PAYLOAD -> FillArrayDataPayloadOp
                Opcode.CHECK_CAST -> CheckCastOp
                Opcode.CMPG_DOUBLE, Opcode.CMPG_FLOAT, Opcode.CMPL_DOUBLE, Opcode.CMPL_FLOAT, Opcode.CMP_LONG -> CmpOp
                Opcode.CONST, Opcode.CONST_16, Opcode.CONST_4, Opcode.CONST_CLASS, Opcode.CONST_HIGH16, Opcode.CONST_STRING, Opcode.CONST_STRING_JUMBO, Opcode.CONST_WIDE, Opcode.CONST_WIDE_16, Opcode.CONST_WIDE_32, Opcode.CONST_WIDE_HIGH16 -> ConstOp
                Opcode.DOUBLE_TO_FLOAT, Opcode.DOUBLE_TO_INT, Opcode.DOUBLE_TO_LONG, Opcode.FLOAT_TO_DOUBLE, Opcode.FLOAT_TO_INT, Opcode.FLOAT_TO_LONG, Opcode.INT_TO_BYTE, Opcode.INT_TO_CHAR, Opcode.INT_TO_DOUBLE, Opcode.INT_TO_FLOAT, Opcode.INT_TO_LONG, Opcode.INT_TO_SHORT, Opcode.LONG_TO_DOUBLE, Opcode.LONG_TO_FLOAT, Opcode.LONG_TO_INT, Opcode.NEG_DOUBLE, Opcode.NEG_FLOAT, Opcode.NEG_INT, Opcode.NEG_LONG, Opcode.NOT_INT, Opcode.NOT_LONG -> UnaryMathOp
                Opcode.FILLED_NEW_ARRAY, Opcode.FILLED_NEW_ARRAY_RANGE -> FilledNewArrayOp
                Opcode.FILL_ARRAY_DATA -> FillArrayDataOp
                Opcode.GOTO, Opcode.GOTO_16, Opcode.GOTO_32 -> GotoOp
                Opcode.IF_EQ, Opcode.IF_GE, Opcode.IF_GT, Opcode.IF_LE, Opcode.IF_LT, Opcode.IF_NE, Opcode.IF_EQZ, Opcode.IF_GEZ, Opcode.IF_GTZ, Opcode.IF_LEZ, Opcode.IF_LTZ, Opcode.IF_NEZ -> IfOp
                Opcode.IGET, Opcode.IGET_BOOLEAN, Opcode.IGET_BYTE, Opcode.IGET_CHAR, Opcode.IGET_OBJECT, Opcode.IGET_SHORT, Opcode.IGET_WIDE -> IGetOp
                Opcode.IPUT, Opcode.IPUT_BOOLEAN, Opcode.IPUT_BYTE, Opcode.IPUT_CHAR, Opcode.IPUT_OBJECT, Opcode.IPUT_SHORT, Opcode.IPUT_WIDE -> IPutOp
                Opcode.INSTANCE_OF -> InstanceOfOp
                Opcode.INVOKE_DIRECT, Opcode.INVOKE_INTERFACE, Opcode.INVOKE_STATIC, Opcode.INVOKE_SUPER, Opcode.INVOKE_VIRTUAL, Opcode.INVOKE_DIRECT_RANGE, Opcode.INVOKE_INTERFACE_RANGE, Opcode.INVOKE_STATIC_RANGE, Opcode.INVOKE_SUPER_RANGE, Opcode.INVOKE_VIRTUAL_RANGE -> InvokeOp
                Opcode.MONITOR_ENTER -> MonitorEnterOp
                Opcode.MONITOR_EXIT -> MonitorExitOp
                Opcode.MOVE, Opcode.MOVE_16, Opcode.MOVE_FROM16, Opcode.MOVE_OBJECT, Opcode.MOVE_OBJECT_16, Opcode.MOVE_OBJECT_FROM16, Opcode.MOVE_WIDE, Opcode.MOVE_WIDE_16, Opcode.MOVE_WIDE_FROM16, Opcode.MOVE_EXCEPTION, Opcode.MOVE_RESULT, Opcode.MOVE_RESULT_OBJECT, Opcode.MOVE_RESULT_WIDE -> MoveOp
                Opcode.NEW_ARRAY -> NewArrayOp
                Opcode.NEW_INSTANCE -> NewInstanceOp
                Opcode.NOP -> NopOp
                Opcode.PACKED_SWITCH, Opcode.SPARSE_SWITCH -> SwitchOp
                Opcode.PACKED_SWITCH_PAYLOAD, Opcode.SPARSE_SWITCH_PAYLOAD -> SwitchPayloadOp
                Opcode.RETURN, Opcode.RETURN_WIDE, Opcode.RETURN_OBJECT -> ReturnOp
                Opcode.RETURN_VOID -> ReturnVoidOp
                Opcode.SGET, Opcode.SGET_BOOLEAN, Opcode.SGET_BYTE, Opcode.SGET_CHAR, Opcode.SGET_OBJECT, Opcode.SGET_SHORT, Opcode.SGET_WIDE -> SGetOp
                Opcode.SPUT, Opcode.SPUT_BOOLEAN, Opcode.SPUT_BYTE, Opcode.SPUT_CHAR, Opcode.SPUT_OBJECT, Opcode.SPUT_SHORT, Opcode.SPUT_WIDE -> SPutOp
                Opcode.THROW -> ThrowOp
                else -> throw RuntimeException("Unimplemented opcode: $op")
            }
        }
    }
}
