begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_enum
DECL|enum|Theme
specifier|public
enum|enum
name|Theme
block|{
comment|// Light themes
DECL|enumConstant|DEFAULT
name|DEFAULT
block|,
DECL|enumConstant|DAY_3024
name|DAY_3024
block|,
DECL|enumConstant|BASE16_LIGHT
name|BASE16_LIGHT
block|,
DECL|enumConstant|ECLIPSE
name|ECLIPSE
block|,
DECL|enumConstant|ELEGANT
name|ELEGANT
block|,
DECL|enumConstant|MDN_LIKE
name|MDN_LIKE
block|,
DECL|enumConstant|NEAT
name|NEAT
block|,
DECL|enumConstant|NEO
name|NEO
block|,
DECL|enumConstant|PARAISO_LIGHT
name|PARAISO_LIGHT
block|,
DECL|enumConstant|SOLARIZED
name|SOLARIZED
block|,
DECL|enumConstant|TTCN
name|TTCN
block|,
DECL|enumConstant|XQ_LIGHT
name|XQ_LIGHT
block|,
DECL|enumConstant|YETI
name|YETI
block|,
comment|// Dark themes
DECL|enumConstant|NIGHT_3024
name|NIGHT_3024
block|,
DECL|enumConstant|ABCDEF
name|ABCDEF
block|,
DECL|enumConstant|AMBIANCE
name|AMBIANCE
block|,
DECL|enumConstant|BASE16_DARK
name|BASE16_DARK
block|,
DECL|enumConstant|BESPIN
name|BESPIN
block|,
DECL|enumConstant|BLACKBOARD
name|BLACKBOARD
block|,
DECL|enumConstant|COBALT
name|COBALT
block|,
DECL|enumConstant|COLORFORTH
name|COLORFORTH
block|,
DECL|enumConstant|DRACULA
name|DRACULA
block|,
DECL|enumConstant|ERLANG_DARK
name|ERLANG_DARK
block|,
DECL|enumConstant|HOPSCOTCH
name|HOPSCOTCH
block|,
DECL|enumConstant|ICECODER
name|ICECODER
block|,
DECL|enumConstant|ISOTOPE
name|ISOTOPE
block|,
DECL|enumConstant|LESSER_DARK
name|LESSER_DARK
block|,
DECL|enumConstant|LIQUIBYTE
name|LIQUIBYTE
block|,
DECL|enumConstant|MATERIAL
name|MATERIAL
block|,
DECL|enumConstant|MBO
name|MBO
block|,
DECL|enumConstant|MIDNIGHT
name|MIDNIGHT
block|,
DECL|enumConstant|MONOKAI
name|MONOKAI
block|,
DECL|enumConstant|NIGHT
name|NIGHT
block|,
DECL|enumConstant|PARAISO_DARK
name|PARAISO_DARK
block|,
DECL|enumConstant|PASTEL_ON_DARK
name|PASTEL_ON_DARK
block|,
DECL|enumConstant|RAILSCASTS
name|RAILSCASTS
block|,
DECL|enumConstant|RUBYBLUE
name|RUBYBLUE
block|,
DECL|enumConstant|SETI
name|SETI
block|,
DECL|enumConstant|THE_MATRIX
name|THE_MATRIX
block|,
DECL|enumConstant|TOMORROW_NIGHT_BRIGHT
name|TOMORROW_NIGHT_BRIGHT
block|,
DECL|enumConstant|TOMORROW_NIGHT_EIGHTIES
name|TOMORROW_NIGHT_EIGHTIES
block|,
DECL|enumConstant|TWILIGHT
name|TWILIGHT
block|,
DECL|enumConstant|VIBRANT_INK
name|VIBRANT_INK
block|,
DECL|enumConstant|XQ_DARK
name|XQ_DARK
block|,
DECL|enumConstant|ZENBURN
name|ZENBURN
block|;
DECL|method|isDark ()
specifier|public
name|boolean
name|isDark
parameter_list|()
block|{
switch|switch
condition|(
name|this
condition|)
block|{
case|case
name|NIGHT_3024
case|:
case|case
name|ABCDEF
case|:
case|case
name|AMBIANCE
case|:
case|case
name|BASE16_DARK
case|:
case|case
name|BESPIN
case|:
case|case
name|BLACKBOARD
case|:
case|case
name|COBALT
case|:
case|case
name|COLORFORTH
case|:
case|case
name|DRACULA
case|:
case|case
name|ERLANG_DARK
case|:
case|case
name|HOPSCOTCH
case|:
case|case
name|ICECODER
case|:
case|case
name|ISOTOPE
case|:
case|case
name|LESSER_DARK
case|:
case|case
name|LIQUIBYTE
case|:
case|case
name|MATERIAL
case|:
case|case
name|MBO
case|:
case|case
name|MIDNIGHT
case|:
case|case
name|MONOKAI
case|:
case|case
name|NIGHT
case|:
case|case
name|PARAISO_DARK
case|:
case|case
name|PASTEL_ON_DARK
case|:
case|case
name|RAILSCASTS
case|:
case|case
name|RUBYBLUE
case|:
case|case
name|SETI
case|:
case|case
name|THE_MATRIX
case|:
case|case
name|TOMORROW_NIGHT_BRIGHT
case|:
case|case
name|TOMORROW_NIGHT_EIGHTIES
case|:
case|case
name|TWILIGHT
case|:
case|case
name|VIBRANT_INK
case|:
case|case
name|XQ_DARK
case|:
case|case
name|ZENBURN
case|:
return|return
literal|true
return|;
case|case
name|DEFAULT
case|:
case|case
name|DAY_3024
case|:
case|case
name|BASE16_LIGHT
case|:
case|case
name|ECLIPSE
case|:
case|case
name|ELEGANT
case|:
case|case
name|MDN_LIKE
case|:
case|case
name|NEAT
case|:
case|case
name|NEO
case|:
case|case
name|PARAISO_LIGHT
case|:
case|case
name|SOLARIZED
case|:
case|case
name|TTCN
case|:
case|case
name|XQ_LIGHT
case|:
case|case
name|YETI
case|:
default|default:
return|return
literal|false
return|;
block|}
block|}
block|}
end_enum

end_unit

