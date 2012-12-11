begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_class
DECL|class|Util
specifier|public
class|class
name|Util
block|{
DECL|field|C
specifier|public
specifier|static
specifier|final
name|UIConstants
name|C
init|=
name|GWT
operator|.
name|create
argument_list|(
name|UIConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|M
specifier|public
specifier|static
specifier|final
name|UIMessages
name|M
init|=
name|GWT
operator|.
name|create
argument_list|(
name|UIMessages
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|highlight (final String text, final String toHighlight)
specifier|public
specifier|static
name|String
name|highlight
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|String
name|toHighlight
parameter_list|)
block|{
specifier|final
name|SafeHtmlBuilder
name|b
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|toHighlight
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|toHighlight
argument_list|)
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toSafeHtml
argument_list|()
operator|.
name|asString
argument_list|()
return|;
block|}
name|int
name|pos
init|=
literal|0
decl_stmt|;
name|int
name|endPos
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|pos
operator|=
name|text
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
name|toHighlight
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|pos
argument_list|)
operator|)
operator|>
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|pos
operator|>
name|endPos
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|text
operator|.
name|substring
argument_list|(
name|endPos
argument_list|,
name|pos
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|endPos
operator|=
name|pos
operator|+
name|toHighlight
operator|.
name|length
argument_list|()
expr_stmt|;
name|b
operator|.
name|openElement
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|text
operator|.
name|substring
argument_list|(
name|pos
argument_list|,
name|endPos
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|closeElement
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|pos
operator|=
name|endPos
expr_stmt|;
block|}
if|if
condition|(
name|endPos
operator|<
name|text
operator|.
name|length
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|text
operator|.
name|substring
argument_list|(
name|endPos
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toSafeHtml
argument_list|()
operator|.
name|asString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

