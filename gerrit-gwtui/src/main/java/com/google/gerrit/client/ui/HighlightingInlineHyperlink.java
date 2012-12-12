begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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

begin_class
DECL|class|HighlightingInlineHyperlink
specifier|public
class|class
name|HighlightingInlineHyperlink
extends|extends
name|InlineHyperlink
block|{
DECL|field|toHighlight
specifier|private
name|String
name|toHighlight
decl_stmt|;
DECL|method|HighlightingInlineHyperlink (final String text, final String token, final String toHighlight)
specifier|public
name|HighlightingInlineHyperlink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|String
name|token
parameter_list|,
specifier|final
name|String
name|toHighlight
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|this
operator|.
name|toHighlight
operator|=
name|toHighlight
expr_stmt|;
name|highlight
argument_list|(
name|text
argument_list|,
name|toHighlight
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|super
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|highlight
argument_list|(
name|text
argument_list|,
name|toHighlight
argument_list|)
expr_stmt|;
block|}
DECL|method|highlight (final String text, final String toHighlight)
specifier|private
name|void
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
name|setHTML
argument_list|(
name|Util
operator|.
name|highlight
argument_list|(
name|text
argument_list|,
name|toHighlight
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

