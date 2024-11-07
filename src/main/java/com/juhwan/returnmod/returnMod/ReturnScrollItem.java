package com.juhwan.returnmod.returnMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;

public class ReturnScrollItem extends Item {

    private  boolean isTeleporting = false;

    public ReturnScrollItem() {
        super(new Item.Settings().maxCount(100));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (isTeleporting) {
            // 이미 사용 중인 경우
            if (!world.isClient) {
                user.sendMessage(Text.of("이미 귀환 중입니다!"), false);
            }
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if (!world.isClient && user instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) user;
            isTeleporting = true; //텔레포트 중으로 상태 변경

            // 채팅 메세지 표시 및 3초 후에 텔레포트 실행
            new Thread(() -> {
                try {
                    player.sendMessage(Text.of("귀환합니다"), false);
                    for (int i = 3; i > 0; i--) {
                        Thread.sleep(1000); //1초 대기
                        player.sendMessage(Text.of(Integer.toString(i)), false);
                    }
                    Thread.sleep(500); //0.5초 대기
                    if (!player.isRemoved()) {
                        BlockPos targetPos = new BlockPos(24, 73, -33); // 텔레포트할 좌표
                        player.teleport((ServerWorld) world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), player.getYaw(), player.getPitch());
                        player.sendMessage(Text.of("귀환하셨습니다!"), false);

                        //아이템 사용 후 제거
                        user.getStackInHand(hand).decrement(1);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    isTeleporting = false;
                }
            }).start();
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}