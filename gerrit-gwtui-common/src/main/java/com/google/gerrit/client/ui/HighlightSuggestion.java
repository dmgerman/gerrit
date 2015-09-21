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
name|safehtml
operator|.
name|shared
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|SuggestOracle
operator|.
name|Suggestion
import|;
end_import

begin_comment
comment|/** A {@code Suggestion} with highlights. */
end_comment

begin_class
DECL|class|HighlightSuggestion
specifier|public
class|class
name|HighlightSuggestion
implements|implements
name|Suggestion
block|{
DECL|field|keyword
specifier|private
specifier|final
name|String
name|keyword
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|HighlightSuggestion (String keyword, String value)
specifier|public
name|HighlightSuggestion
parameter_list|(
name|String
name|keyword
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|keyword
operator|=
name|keyword
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDisplayString ()
specifier|public
name|String
name|getDisplayString
parameter_list|()
block|{
name|int
name|start
init|=
literal|0
decl_stmt|;
name|int
name|keyLen
init|=
name|keyword
operator|.
name|length
argument_list|()
decl_stmt|;
name|SafeHtmlBuilder
name|builder
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|index
init|=
name|value
operator|.
name|indexOf
argument_list|(
name|keyword
argument_list|,
name|start
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|==
operator|-
literal|1
condition|)
block|{
name|builder
operator|.
name|appendEscaped
argument_list|(
name|value
operator|.
name|substring
argument_list|(
name|start
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|builder
operator|.
name|appendEscaped
argument_list|(
name|value
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|appendHtmlConstant
argument_list|(
literal|"<strong>"
argument_list|)
expr_stmt|;
name|start
operator|=
name|index
operator|+
name|keyLen
expr_stmt|;
name|builder
operator|.
name|appendEscaped
argument_list|(
name|value
operator|.
name|substring
argument_list|(
name|index
argument_list|,
name|start
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|appendHtmlConstant
argument_list|(
literal|"</strong>"
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toSafeHtml
argument_list|()
operator|.
name|asString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getReplacementString ()
specifier|public
name|String
name|getReplacementString
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

