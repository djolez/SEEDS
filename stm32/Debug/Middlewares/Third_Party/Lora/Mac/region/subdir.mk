################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Middlewares/Third_Party/Lora/Mac/region/Region.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionAS923.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionAU915.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionCN470.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionCN779.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionCommon.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionEU433.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionEU868.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionIN865.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionKR920.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionUS915-Hybrid.c \
../Middlewares/Third_Party/Lora/Mac/region/RegionUS915.c 

OBJS += \
./Middlewares/Third_Party/Lora/Mac/region/Region.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionAS923.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionAU915.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionCN470.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionCN779.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionCommon.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionEU433.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionEU868.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionIN865.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionKR920.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionUS915-Hybrid.o \
./Middlewares/Third_Party/Lora/Mac/region/RegionUS915.o 

C_DEPS += \
./Middlewares/Third_Party/Lora/Mac/region/Region.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionAS923.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionAU915.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionCN470.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionCN779.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionCommon.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionEU433.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionEU868.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionIN865.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionKR920.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionUS915-Hybrid.d \
./Middlewares/Third_Party/Lora/Mac/region/RegionUS915.d 


# Each subdirectory must supply rules for building sources it contributes
Middlewares/Third_Party/Lora/Mac/region/%.o: ../Middlewares/Third_Party/Lora/Mac/region/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo %cd%
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -D__weak=__attribute__((weak)) -D__packed=__attribute__((__packed__)) -DUSE_HAL_DRIVER -DSTM32L476xx -DUSE_STM32L4XX_NUCLEO -DUSE_BAND_868 -DUSE_MODEM_LORA -DREGION_EU868 -DDEBUG -I"C:/Users/Djordje/workspace/thesis/stm32/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/STM32L4xx_HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/STM32L4xx_HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/Device/ST/STM32L4xx/Include" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/Include" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/libs/jsmn" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/devices" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/common" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/libs/tm_stm32_delay" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/devices/dht11" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/devices/ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/devices/switch" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/devices/ds18b20/tm_stm32f4_ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/Src/devices/ds18b20/tm_stm32f4_onewire" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272mb2das" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Third_Party/Lora/Core" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Third_Party/Lora/Crypto" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Third_Party/Lora/Mac" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Third_Party/Lora/Phy" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Third_Party/Lora/Utilities" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Inc/lora" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/Components/sx1272"  -Og -g3 -Wall -fmessage-length=0 -ffunction-sections -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


