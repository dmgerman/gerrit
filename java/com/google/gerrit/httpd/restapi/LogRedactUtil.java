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
DECL|package|com.google.gerrit.httpd.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
operator|.
name|RestApiServlet
operator|.
name|XD_AUTHORIZATION
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|Url
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_class
DECL|class|LogRedactUtil
specifier|public
class|class
name|LogRedactUtil
block|{
DECL|field|REDACT_PARAM
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|REDACT_PARAM
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|XD_AUTHORIZATION
argument_list|)
decl_stmt|;
DECL|method|LogRedactUtil ()
specifier|private
name|LogRedactUtil
parameter_list|()
block|{}
comment|/**    * Redacts sensitive information such as an access token from the query string to make it suitable    * for logging.    */
annotation|@
name|VisibleForTesting
DECL|method|redactQueryString (String qs)
specifier|public
specifier|static
name|String
name|redactQueryString
parameter_list|(
name|String
name|qs
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|kvPair
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|'&'
argument_list|)
operator|.
name|split
argument_list|(
name|qs
argument_list|)
control|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|i
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'='
argument_list|)
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|split
argument_list|(
name|kvPair
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
if|if
condition|(
name|REDACT_PARAM
operator|.
name|contains
argument_list|(
name|Url
operator|.
name|decode
argument_list|(
name|key
argument_list|)
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|'*'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|append
argument_list|(
name|i
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

