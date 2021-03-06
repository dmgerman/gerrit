begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|common
operator|.
name|data
operator|.
name|LabelType
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
name|RestResource
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
name|RestView
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
name|TypeLiteral
import|;
end_import

begin_class
DECL|class|LabelResource
specifier|public
class|class
name|LabelResource
implements|implements
name|RestResource
block|{
DECL|field|LABEL_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|LabelResource
argument_list|>
argument_list|>
name|LABEL_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|LabelResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectResource
name|project
decl_stmt|;
DECL|field|labelType
specifier|private
specifier|final
name|LabelType
name|labelType
decl_stmt|;
DECL|method|LabelResource (ProjectResource project, LabelType labelType)
specifier|public
name|LabelResource
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|LabelType
name|labelType
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|labelType
operator|=
name|labelType
expr_stmt|;
block|}
DECL|method|getProject ()
specifier|public
name|ProjectResource
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
DECL|method|getLabelType ()
specifier|public
name|LabelType
name|getLabelType
parameter_list|()
block|{
return|return
name|labelType
return|;
block|}
block|}
end_class

end_unit

