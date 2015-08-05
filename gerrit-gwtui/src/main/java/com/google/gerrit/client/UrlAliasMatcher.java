begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|regexp
operator|.
name|shared
operator|.
name|RegExp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|UrlAliasMatcher
specifier|public
class|class
name|UrlAliasMatcher
block|{
DECL|field|globalUrlAliases
specifier|private
specifier|final
name|Map
argument_list|<
name|RegExp
argument_list|,
name|String
argument_list|>
name|globalUrlAliases
decl_stmt|;
DECL|method|UrlAliasMatcher (Map<String, String> globalUrlAliases)
name|UrlAliasMatcher
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|globalUrlAliases
parameter_list|)
block|{
name|this
operator|.
name|globalUrlAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
if|if
condition|(
name|globalUrlAliases
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|globalUrlAliases
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|this
operator|.
name|globalUrlAliases
operator|.
name|put
argument_list|(
name|RegExp
operator|.
name|compile
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|replace (String token)
specifier|public
name|String
name|replace
parameter_list|(
name|String
name|token
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|RegExp
argument_list|,
name|String
argument_list|>
name|e
range|:
name|globalUrlAliases
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|RegExp
name|pat
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|pat
operator|.
name|exec
argument_list|(
name|token
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|pat
operator|.
name|replace
argument_list|(
name|token
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
name|token
return|;
block|}
block|}
end_class

end_unit

