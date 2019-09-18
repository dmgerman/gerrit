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
DECL|package|com.google.gerrit.gpg.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
operator|.
name|api
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
name|server
operator|.
name|api
operator|.
name|ApiUtil
operator|.
name|asRestApiException
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
name|api
operator|.
name|accounts
operator|.
name|GpgKeyApi
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
name|common
operator|.
name|GpgKeyInfo
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
name|common
operator|.
name|Input
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
name|RestApiException
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
name|gpg
operator|.
name|server
operator|.
name|DeleteGpgKey
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
name|gpg
operator|.
name|server
operator|.
name|GpgKey
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
name|gpg
operator|.
name|server
operator|.
name|GpgKeys
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_class
DECL|class|GpgKeyApiImpl
specifier|public
class|class
name|GpgKeyApiImpl
implements|implements
name|GpgKeyApi
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (GpgKey rsrc)
name|GpgKeyApiImpl
name|create
parameter_list|(
name|GpgKey
name|rsrc
parameter_list|)
function_decl|;
block|}
DECL|field|get
specifier|private
specifier|final
name|GpgKeys
operator|.
name|Get
name|get
decl_stmt|;
DECL|field|delete
specifier|private
specifier|final
name|DeleteGpgKey
name|delete
decl_stmt|;
DECL|field|rsrc
specifier|private
specifier|final
name|GpgKey
name|rsrc
decl_stmt|;
annotation|@
name|Inject
DECL|method|GpgKeyApiImpl (GpgKeys.Get get, DeleteGpgKey delete, @Assisted GpgKey rsrc)
name|GpgKeyApiImpl
parameter_list|(
name|GpgKeys
operator|.
name|Get
name|get
parameter_list|,
name|DeleteGpgKey
name|delete
parameter_list|,
annotation|@
name|Assisted
name|GpgKey
name|rsrc
parameter_list|)
block|{
name|this
operator|.
name|get
operator|=
name|get
expr_stmt|;
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
name|this
operator|.
name|rsrc
operator|=
name|rsrc
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|GpgKeyInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|get
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot get GPG key"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|delete ()
specifier|public
name|void
name|delete
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
name|delete
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|,
operator|new
name|Input
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PGPException
decl||
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot delete GPG key"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

