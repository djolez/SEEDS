################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Drivers/HAL_Driver/Src/stm32l4xx_hal.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_adc.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_adc_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_cortex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_dma.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_flash.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_flash_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_flash_ramfunc.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_gpio.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_i2c.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_i2c_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_lptim.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_pwr.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_pwr_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_rcc.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_rcc_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_spi.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_spi_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_tim.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_tim_ex.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_uart.c \
../Drivers/HAL_Driver/Src/stm32l4xx_hal_uart_ex.c 

OBJS += \
./Drivers/HAL_Driver/Src/stm32l4xx_hal.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_adc.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_adc_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_cortex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_dma.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_flash.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_flash_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_flash_ramfunc.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_gpio.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_i2c.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_i2c_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_lptim.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_pwr.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_pwr_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_rcc.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_rcc_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_spi.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_spi_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_tim.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_tim_ex.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_uart.o \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_uart_ex.o 

C_DEPS += \
./Drivers/HAL_Driver/Src/stm32l4xx_hal.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_adc.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_adc_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_cortex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_dma.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_flash.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_flash_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_flash_ramfunc.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_gpio.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_i2c.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_i2c_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_lptim.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_pwr.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_pwr_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_rcc.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_rcc_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_spi.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_spi_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_tim.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_tim_ex.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_uart.d \
./Drivers/HAL_Driver/Src/stm32l4xx_hal_uart_ex.d 


# Each subdirectory must supply rules for building sources it contributes
Drivers/HAL_Driver/Src/%.o: ../Drivers/HAL_Driver/Src/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: MCU GCC Compiler'
	@echo %cd%
	arm-none-eabi-gcc -mcpu=cortex-m4 -mthumb -mfloat-abi=hard -mfpu=fpv4-sp-d16 -DSTM32L4 -DSTM32L476RGTx -DNUCLEO_L476RG -DSTM32 -DDEBUG -DUSE_HAL_DRIVER -DSTM32L476xx -DUSE_BAND_868 -DUSE_MODEM_LORA -DUSE_STM32L4XX_NUCLEO -DREGION_EU868 -I"C:/Users/Djordje/workspace/thesis/stm32/inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Conf" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Core" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Crypto" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Phy" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Utilities" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1272mb2das" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/src/common" -I"C:/Users/Djordje/workspace/thesis/stm32/src/tm_stm32_delay" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20/tm_stm32f4_onewire" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/ds18b20" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/dht11" -I"C:/Users/Djordje/workspace/thesis/stm32/src/devices/switch" -I"C:/Users/Djordje/workspace/thesis/stm32/Middlewares/Lora/Mac/region" -I"C:/Users/Djordje/workspace/thesis/stm32/CMSIS/core" -I"C:/Users/Djordje/workspace/thesis/stm32/CMSIS/device" -I"C:/Users/Djordje/workspace/thesis/stm32/HAL_Driver/Inc/Legacy" -I"C:/Users/Djordje/workspace/thesis/stm32/HAL_Driver/Inc" -I"C:/Users/Djordje/workspace/thesis/stm32/Utilities/STM32L4xx_Nucleo" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1276mb1las" -I"C:/Users/Djordje/workspace/thesis/stm32/Drivers/BSP/sx1276" -O0 -g3 -Wall -fmessage-length=0 -ffunction-sections -c -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


