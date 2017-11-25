################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Middlewares/Lora/Mac/region/Region.c \
../Middlewares/Lora/Mac/region/RegionAS923.c \
../Middlewares/Lora/Mac/region/RegionAU915.c \
../Middlewares/Lora/Mac/region/RegionCN470.c \
../Middlewares/Lora/Mac/region/RegionCN779.c \
../Middlewares/Lora/Mac/region/RegionCommon.c \
../Middlewares/Lora/Mac/region/RegionEU433.c \
../Middlewares/Lora/Mac/region/RegionEU868.c \
../Middlewares/Lora/Mac/region/RegionIN865.c \
../Middlewares/Lora/Mac/region/RegionKR920.c \
../Middlewares/Lora/Mac/region/RegionUS915-Hybrid.c \
../Middlewares/Lora/Mac/region/RegionUS915.c 

OBJS += \
./Middlewares/Lora/Mac/region/Region.o \
./Middlewares/Lora/Mac/region/RegionAS923.o \
./Middlewares/Lora/Mac/region/RegionAU915.o \
./Middlewares/Lora/Mac/region/RegionCN470.o \
./Middlewares/Lora/Mac/region/RegionCN779.o \
./Middlewares/Lora/Mac/region/RegionCommon.o \
./Middlewares/Lora/Mac/region/RegionEU433.o \
./Middlewares/Lora/Mac/region/RegionEU868.o \
./Middlewares/Lora/Mac/region/RegionIN865.o \
./Middlewares/Lora/Mac/region/RegionKR920.o \
./Middlewares/Lora/Mac/region/RegionUS915-Hybrid.o \
./Middlewares/Lora/Mac/region/RegionUS915.o 

C_DEPS += \
./Middlewares/Lora/Mac/region/Region.d \
./Middlewares/Lora/Mac/region/RegionAS923.d \
./Middlewares/Lora/Mac/region/RegionAU915.d \
./Middlewares/Lora/Mac/region/RegionCN470.d \
./Middlewares/Lora/Mac/region/RegionCN779.d \
./Middlewares/Lora/Mac/region/RegionCommon.d \
./Middlewares/Lora/Mac/region/RegionEU433.d \
./Middlewares/Lora/Mac/region/RegionEU868.d \
./Middlewares/Lora/Mac/region/RegionIN865.d \
./Middlewares/Lora/Mac/region/RegionKR920.d \
./Middlewares/Lora/Mac/region/RegionUS915-Hybrid.d \
./Middlewares/Lora/Mac/region/RegionUS915.d 


# Each subdirectory must supply rules for building sources it contributes
Middlewares/Lora/Mac/region/%.o: ../Middlewares/Lora/Mac/region/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo %cd%
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -DSTM32L4 -DSTM32L476RGTx -DNUCLEO_L476RG -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -DUSE_BAND_868 -DUSE_MODEM_LORA -DUSE_STM32L4XX_NUCLEO -DREGION_EU868 -I"C:/Users/Djordje/workspace/thesis/stm32/inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Conf" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Core" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Crypto" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Phy" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Utilities" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272mb2das" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/src/common" -I"C:/Users/Djordje/workspace/thesis/stm32/src/tm_stm32_delay" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_onewire" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/dht11" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/switch" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac/region" -I"C:/Users/Djordje/workspace/thesis/stm32/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Utilities/STM32L4xx_Nucleo" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


