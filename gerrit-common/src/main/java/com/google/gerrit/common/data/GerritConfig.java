begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_class
DECL|class|GerritConfig
specifier|public
class|class
name|GerritConfig
implements|implements
name|Cloneable
block|{
DECL|field|documentationAvailable
specifier|protected
name|boolean
name|documentationAvailable
decl_stmt|;
DECL|method|isDocumentationAvailable ()
specifier|public
name|boolean
name|isDocumentationAvailable
parameter_list|()
block|{
return|return
name|documentationAvailable
return|;
block|}
DECL|method|setDocumentationAvailable (final boolean available)
specifier|public
name|void
name|setDocumentationAvailable
parameter_list|(
specifier|final
name|boolean
name|available
parameter_list|)
block|{
name|documentationAvailable
operator|=
name|available
expr_stmt|;
block|}
block|}
end_class

end_unit

