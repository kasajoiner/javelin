🔥Нове замовлення ${id}.
<#if tag??>
    @${tag} +${phone}
<#else>
    +${phone}
</#if>
Всього замовлень: ${orderSize}