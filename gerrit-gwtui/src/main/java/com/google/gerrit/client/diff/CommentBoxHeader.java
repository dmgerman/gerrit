begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|AvatarImage
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
name|client
operator|.
name|FormatUtil
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
name|client
operator|.
name|account
operator|.
name|AccountInfo
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
name|gwt
operator|.
name|dom
operator|.
name|client
operator|.
name|Element
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|Composite
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
name|HTMLPanel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/**  * An HtmlPanel representing the header of a CommentBox, displaying  * the author's avatar (if applicable), the author's name, the summary,  * and the date.  */
end_comment

begin_class
DECL|class|CommentBoxHeader
class|class
name|CommentBoxHeader
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|CommentBoxHeader
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|avatar
name|AvatarImage
name|avatar
decl_stmt|;
annotation|@
name|UiField
DECL|field|name
name|Element
name|name
decl_stmt|;
annotation|@
name|UiField
DECL|field|summary
name|Element
name|summary
decl_stmt|;
annotation|@
name|UiField
DECL|field|date
name|Element
name|date
decl_stmt|;
DECL|method|CommentBoxHeader (AccountInfo author, Timestamp when, boolean isDraft)
name|CommentBoxHeader
parameter_list|(
name|AccountInfo
name|author
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|boolean
name|isDraft
parameter_list|)
block|{
comment|// TODO: Set avatar's display to none if we get a 404.
name|avatar
operator|=
operator|new
name|AvatarImage
argument_list|(
name|author
argument_list|,
literal|26
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|dateText
init|=
name|FormatUtil
operator|.
name|shortFormatDayTime
argument_list|(
name|when
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDraft
condition|)
block|{
name|name
operator|.
name|setInnerText
argument_list|(
literal|"(Draft)"
argument_list|)
expr_stmt|;
name|date
operator|.
name|setInnerText
argument_list|(
literal|"Draft saved at "
operator|+
name|dateText
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|.
name|setInnerText
argument_list|(
name|FormatUtil
operator|.
name|name
argument_list|(
name|author
argument_list|)
argument_list|)
expr_stmt|;
name|date
operator|.
name|setInnerText
argument_list|(
name|dateText
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setSummaryText (String message)
name|void
name|setSummaryText
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|summary
operator|.
name|setInnerText
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

