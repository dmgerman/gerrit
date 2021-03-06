begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
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
name|extensions
operator|.
name|client
operator|.
name|ChangeKind
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
DECL|class|RevisionInfo
specifier|public
class|class
name|RevisionInfo
block|{
comment|// ActionJson#copy(List, RevisionInfo) must be adapted if new fields are added that are not
comment|// protected by any ListChangesOption.
DECL|field|isCurrent
specifier|public
specifier|transient
name|boolean
name|isCurrent
decl_stmt|;
DECL|field|kind
specifier|public
name|ChangeKind
name|kind
decl_stmt|;
DECL|field|_number
specifier|public
name|int
name|_number
decl_stmt|;
DECL|field|created
specifier|public
name|Timestamp
name|created
decl_stmt|;
DECL|field|uploader
specifier|public
name|AccountInfo
name|uploader
decl_stmt|;
DECL|field|ref
specifier|public
name|String
name|ref
decl_stmt|;
DECL|field|fetch
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|FetchInfo
argument_list|>
name|fetch
decl_stmt|;
DECL|field|commit
specifier|public
name|CommitInfo
name|commit
decl_stmt|;
DECL|field|files
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|FileInfo
argument_list|>
name|files
decl_stmt|;
DECL|field|actions
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|actions
decl_stmt|;
DECL|field|commitWithFooters
specifier|public
name|String
name|commitWithFooters
decl_stmt|;
DECL|field|pushCertificate
specifier|public
name|PushCertificateInfo
name|pushCertificate
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
block|}
end_class

end_unit

