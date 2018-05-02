"%WIX%\bin\heat.exe" dir ../Documentation/fr/images -cg FrenchDocumentationFiles -dr FrenchImages -var "var.FrenchImages" -scom -frag -srd -sreg -gg -g1 -o HarvestedFrenchDocumentation.wxs
"%WIX%\bin\heat.exe" dir ../Documentation/en/images -cg EnglishDocumentationFiles -dr EnglishImages -var "var.EnglishImages" -scom -frag -srd -sreg -gg -g1 -o HarvestedEnglishDocumentation.wxs
