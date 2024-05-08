#if FABRIC_LOADER && MC_VERSION >= "11500"
package mirsario.cameraoverhaul.fabric.mixins;

import mirsario.cameraoverhaul.abstractions.*;
import mirsario.cameraoverhaul.callbacks.*;
import mirsario.cameraoverhaul.structures.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.mojang.blaze3d.vertex.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin
{
	@Shadow @Final private Camera mainCamera;

	@Inject(method = "renderLevel", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
		shift = At.Shift.BEFORE
	))
	private void PostCameraUpdate(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo ci)
	{
		Transform cameraTransform = new Transform(mainCamera.getPosition(), new Vec3(mainCamera.getXRot(), mainCamera.getYRot(), 0));

		cameraTransform = ModifyCameraTransformCallback.EVENT.Invoker().ModifyCameraTransform(mainCamera, cameraTransform);

		//matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)cameraTransform.eulerRot.z));
		MathAbstractions.RotateMatrixByAxis(matrix, 0f, 0f, 1f, (float)cameraTransform.eulerRot.z);
	}
}
#endif