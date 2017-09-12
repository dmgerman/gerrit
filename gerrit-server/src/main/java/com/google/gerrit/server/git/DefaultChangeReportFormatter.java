begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|server
operator|.
name|ChangeUtil
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
name|server
operator|.
name|config
operator|.
name|CanonicalWebUrl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|DefaultChangeReportFormatter
specifier|public
class|class
name|DefaultChangeReportFormatter
implements|implements
name|ChangeReportFormatter
block|{
DECL|field|canonicalWebUrl
specifier|private
specifier|final
name|String
name|canonicalWebUrl
decl_stmt|;
annotation|@
name|Inject
DECL|method|DefaultChangeReportFormatter (@anonicalWebUrl String canonicalWebUrl)
name|DefaultChangeReportFormatter
parameter_list|(
annotation|@
name|CanonicalWebUrl
name|String
name|canonicalWebUrl
parameter_list|)
block|{
name|this
operator|.
name|canonicalWebUrl
operator|=
name|canonicalWebUrl
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newChange (ChangeReportFormatter.Input input)
specifier|public
name|String
name|newChange
parameter_list|(
name|ChangeReportFormatter
operator|.
name|Input
name|input
parameter_list|)
block|{
return|return
name|formatChangeUrl
argument_list|(
name|canonicalWebUrl
argument_list|,
name|input
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|changeUpdated (ChangeReportFormatter.Input input)
specifier|public
name|String
name|changeUpdated
parameter_list|(
name|ChangeReportFormatter
operator|.
name|Input
name|input
parameter_list|)
block|{
return|return
name|formatChangeUrl
argument_list|(
name|canonicalWebUrl
argument_list|,
name|input
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|changeClosed (ChangeReportFormatter.Input input)
specifier|public
name|String
name|changeClosed
parameter_list|(
name|ChangeReportFormatter
operator|.
name|Input
name|input
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"change %s closed"
argument_list|,
name|ChangeUtil
operator|.
name|formatChangeUrl
argument_list|(
name|canonicalWebUrl
argument_list|,
name|input
operator|.
name|change
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|formatChangeUrl (String url, Input input)
specifier|private
name|String
name|formatChangeUrl
parameter_list|(
name|String
name|url
parameter_list|,
name|Input
name|input
parameter_list|)
block|{
name|StringBuilder
name|m
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"  "
argument_list|)
operator|.
name|append
argument_list|(
name|ChangeUtil
operator|.
name|formatChangeUrl
argument_list|(
name|url
argument_list|,
name|input
operator|.
name|change
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|ChangeUtil
operator|.
name|cropSubject
argument_list|(
name|input
operator|.
name|subject
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|isDraft
argument_list|()
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|" [DRAFT]"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|isEdit
argument_list|()
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|" [EDIT]"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|isPrivate
argument_list|()
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|" [PRIVATE]"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|isWorkInProgress
argument_list|()
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|" [WIP]"
argument_list|)
expr_stmt|;
block|}
return|return
name|m
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

