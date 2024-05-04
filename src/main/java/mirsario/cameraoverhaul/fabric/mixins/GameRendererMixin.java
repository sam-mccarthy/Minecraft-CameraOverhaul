#if FABRIC_LOADER && MC_VERSION >= "11500"
package mirsario.cameraoverhaul.fabric.mixins;

import mirsario.cameraoverhaul.abstractions.*;
import mirsario.cameraoverhaul.callbacks.*;
import mirsario.cameraoverhaul.structures.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin
{
	@Shadow @Final private Camera camera;

	@Inject(method = "renderWorld", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/Camera;update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V",
		shift = At.Shift.BEFORE
	))
	private void PostCameraUpdate(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci)
	{
		Transform cameraTransform = new Transform(camera.getPos(), new Vec3d(camera.getPitch(), camera.getYaw(), 0d));

		cameraTransform = ModifyCameraTransformCallback.EVENT.Invoker().ModifyCameraTransform(camera, cameraTransform);

		//matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)cameraTransform.eulerRot.z));
		MathAbstractions.RotateMatrixByAxis(matrix, 0f, 0f, 1f, (float)cameraTransform.eulerRot.z);
	}
}
#endif