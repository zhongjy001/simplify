package org.cf.smalivm.opcode

import org.cf.smalivm.configuration.Configuration
import org.cf.smalivm.dex.CommonTypes
import org.cf.smalivm.dex.SmaliClassLoader
import org.cf.smalivm.type.ClassManager
import org.cf.smalivm.type.VirtualType
import org.cf.smalivm2.ExecutionNode
import org.cf.smalivm2.OpChild
import org.cf.smalivm2.Value
import org.cf.util.Utils
import org.jf.dexlib2.builder.MethodLocation
import org.jf.dexlib2.iface.instruction.formats.Instruction22c
import org.jf.dexlib2.iface.reference.TypeReference
import org.slf4j.LoggerFactory

class InstanceOfOp internal constructor(
    location: MethodLocation,
    child: MethodLocation,
    private val destRegister: Int,
    private val arg1Register: Int,
    private val referenceType: VirtualType,
    private val classManager: ClassManager
) : Op(location, child) {

    override val registersReadCount = 1
    override val registersAssignedCount = 1

    override fun execute(node: ExecutionNode): Array<out OpChild> {
        val state = node.state
        val instance = node.state.readRegister(arg1Register)
        if (instance.value == null) {
            state.assignRegister(destRegister, Value.wrap(false, CommonTypes.BOOL))
            return collectChildren()
        }
        for (typeName in instance.declaredAndValueTypeNames) {
            val itemType = classManager.getVirtualType(typeName)
            if (itemType.instanceOf(referenceType)) {
                state.assignRegister(destRegister, Value.wrap(true, CommonTypes.BOOL))
                return collectChildren()
            }
        }
        if (instance.isUnknown) {
            // Since value is unknown, we can't get the actual type of the value and declared type isn't specific enough.
            state.assignRegister(destRegister, Value.unknown(CommonTypes.BOOL))
        } else {
            state.assignRegister(destRegister, Value.wrap(false, CommonTypes.BOOL))
        }
        return collectChildren()
    }

    override fun toString() = "$name r$destRegister, $arg1Register, $referenceType"

    companion object : OpFactory {
        private val log = LoggerFactory.getLogger(InstanceOfOp::class.java.simpleName)
        override fun build(
            location: MethodLocation,
            addressToLocation: Map<Int, MethodLocation>,
            classManager: ClassManager,
            classLoader: SmaliClassLoader,
            configuration: Configuration
        ): Op {
            val child = Utils.getNextLocation(location, addressToLocation)
            val instr = location.instruction as Instruction22c
            val destRegister = instr.registerA
            val arg1Register = instr.registerB
            val typeReference = instr.reference as TypeReference
            val referenceType = classManager.getVirtualType(typeReference)
            return InstanceOfOp(location, child, destRegister, arg1Register, referenceType, classManager)
        }
    }
}
