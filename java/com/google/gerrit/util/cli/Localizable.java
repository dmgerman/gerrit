begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.util.cli
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|util
operator|.
name|cli
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|Localizable
specifier|public
class|class
name|Localizable
implements|implements
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Localizable
block|{
DECL|field|format
specifier|private
specifier|final
name|String
name|format
decl_stmt|;
annotation|@
name|Override
DECL|method|formatWithLocale (Locale locale, Object... args)
specifier|public
name|String
name|formatWithLocale
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|locale
argument_list|,
name|format
argument_list|,
name|args
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|format (Object... args)
specifier|public
name|String
name|format
parameter_list|(
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|format
argument_list|,
name|args
argument_list|)
return|;
block|}
DECL|method|Localizable (String format)
specifier|private
name|Localizable
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
DECL|method|localizable (String format)
specifier|public
specifier|static
name|Localizable
name|localizable
parameter_list|(
name|String
name|format
parameter_list|)
block|{
return|return
operator|new
name|Localizable
argument_list|(
name|format
argument_list|)
return|;
block|}
block|}
end_class

end_unit

